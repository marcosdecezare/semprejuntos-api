package com.sempremjuntos.api.services;

import com.sempremjuntos.api.entities.LocationDTO;
import com.sempremjuntos.api.repositories.DeviceLastLocationRepository;
import com.sempremjuntos.api.repositories.LbsReadingsRepository;
import com.sempremjuntos.api.repositories.LocationReadingsRepository;
import com.sempremjuntos.api.repositories.WifiReadingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Serviço responsável por obter a localização atual de um dispositivo.
 * Prioriza GPS, faz fallback via LBS (Google Geolocation API) e utiliza cache local.
 * Retorna um LocationDTO contendo classificação de precisão e descrição amigável.
 */
@Service
public class LocationService {

    @Autowired private LocationReadingsRepository gpsRepo;
    @Autowired private LbsReadingsRepository lbsRepo;
    @Autowired private DeviceLastLocationRepository cacheRepo;
    @Autowired private GeoLocationResolverService geo;
    @Autowired private WifiReadingsRepository wifiRepo;
    /**
     * Obtém a localização mais recente e válida do dispositivo.
     * Fluxo: GPS válido → LBS (Google) → Cache.
     */

    /**
    public Optional<LocationDTO> getCurrentLocation(Integer deviceId) {
        try {
            // 1️⃣ GPS válido (latitude/longitude ≠ 0), mas desconsiderando GPS muito antigo (> 5 min)
            var gpsOpt = gpsRepo.findLatestValidGps(deviceId);
            if (gpsOpt.isPresent()) {
                var gps = gpsOpt.get();

                var now = OffsetDateTime.now();
                long gpsMaxAgeMinutes = 5L;

                if (gps.createdAt.isBefore(now.minusMinutes(gpsMaxAgeMinutes))) {
                    System.out.printf(
                            "[GPS] Último GPS válido muito antigo para device=%d. created_at=%s, now=%s, limite=%d min%n",
                            deviceId, gps.createdAt, now, gpsMaxAgeMinutes
                    );
                } else {
                    var sig = "GPS:" + gps.id;
                    var lastSig = cacheRepo.findSignature(deviceId).orElse(null);

                    if (!sig.equals(lastSig)) {
                        System.out.printf(
                                "[GPS] Novo registro detectado. Device=%d | ID=%d | lat=%.7f | lon=%.7f | created_at=%s%n",
                                gps.deviceId, gps.id, gps.lat, gps.lon, gps.createdAt
                        );
                        cacheRepo.upsert(deviceId, gps.lat, gps.lon, "GPS", 10.0, sig, gps.createdAt);
                    } else {
                        System.out.printf(
                                "[GPS] Nenhuma atualização. Device=%d (assinatura igual) | last_created_at=%s%n",
                                deviceId, gps.createdAt
                        );
                    }

                    var cached = cacheRepo.findByDeviceId(deviceId);
                    if (cached.isPresent()) {
                        var loc = cached.get();
                        System.out.printf(
                                "[GPS] Retornando coordenadas válidas do cache. Device=%d | resolved_at=%s%n",
                                deviceId, loc.getTimestamp()
                        );
                        return Optional.of(new LocationDTO(
                                loc.getDeviceId(),
                                loc.getLatitude(),
                                loc.getLongitude(),
                                loc.getSource(),
                                loc.getAccuracyMeters(),
                                loc.getTimestamp()
                        ));
                    }
                }
            } else {
                System.out.printf("[GPS] Nenhum GPS válido encontrado para device=%d%n", deviceId);
            }

            // 2️⃣ Fallback LBS + Wi-Fi (usa Google Geolocation API)
            var lbsOpt = lbsRepo.findLatestLbs(deviceId);
            if (lbsOpt.isPresent()) {
                var lbs = lbsOpt.get();
                var sig = lbs.signature();
                var lastSig = cacheRepo.findSignature(deviceId).orElse(null);

                // Busca últimas redes Wi-Fi vistas pelo dispositivo (por ex. 10)
                var wifiRows = wifiRepo.findRecentWifi(deviceId, 10);
                var wifiPoints = wifiRows.stream()
                        .map(w -> new GeoLocationResolverService.WifiAccessPoint(
                                w.macAddress,
                                w.signalStrength
                        ))
                        .toList();

                if (!sig.equals(lastSig)) {
                    System.out.printf("[LBS] Tentando resolver via Google. Device=%d | %s | created_at=%s | wifiCount=%d%n",
                            deviceId, sig, lbs.createdAt, wifiPoints.size());

                    var resolved = wifiPoints.isEmpty()
                            ? geo.resolveLbs(lbs.mcc, lbs.mnc, lbs.lac, lbs.cid, lbs.signalStrength)
                            : geo.resolveLbsWithWifi(lbs.mcc, lbs.mnc, lbs.lac, lbs.cid, lbs.signalStrength, wifiPoints);

                    if (resolved.isPresent()) {
                        var p = resolved.get();
                        if (p.latitude() != 0.0 && p.longitude() != 0.0) {
                            System.out.printf("[LBS] Resolved: lat=%.7f lon=%.7f acc=%.1fm (fonte=%s)%n",
                                    p.latitude(), p.longitude(), p.accuracyMeters(),
                                    wifiPoints.isEmpty() ? "LBS" : "LBS+WIFI");

                            cacheRepo.upsert(deviceId, p.latitude(), p.longitude(),
                                    wifiPoints.isEmpty() ? "LBS" : "LBS",
                                    p.accuracyMeters(), sig, OffsetDateTime.now());
                        } else {
                            System.out.printf("[LBS] Coordenadas inválidas (0,0). Ignorando.%n");
                        }
                    } else {
                        System.out.printf("[LBS] Nenhuma coordenada retornada. Device=%d%n", deviceId);
                    }
                } else {
                    System.out.printf("[LBS] Assinatura igual. Usando cache atual. Device=%d%n", deviceId);
                }

                var cached = cacheRepo.findByDeviceId(deviceId);
                if (cached.isPresent()) {
                    var loc = cached.get();
                    System.out.printf("[LBS] Retornando coordenadas válidas do cache. Device=%d | resolved_at=%s%n",
                            deviceId, loc.getTimestamp());
                    return Optional.of(new LocationDTO(
                            loc.getDeviceId(),
                            loc.getLatitude(),
                            loc.getLongitude(),
                            loc.getSource(),
                            loc.getAccuracyMeters(),
                            loc.getTimestamp()
                    ));
                }
            } else {
                System.out.printf("[LBS] Nenhum registro LBS encontrado para device=%d%n", deviceId);
            }

            // 3️⃣ Último cache válido (quando GPS/LBS não retornam nada)
            var cached = cacheRepo.findByDeviceId(deviceId);
            if (cached.isPresent()) {
                var loc = cached.get();
                System.out.printf("[CACHE] Retornando última posição conhecida. Device=%d | Fonte=%s | acc=%.1f | resolved_at=%s%n",
                        deviceId, loc.getSource(), loc.getAccuracyMeters(), loc.getTimestamp());
                return Optional.of(new LocationDTO(
                        loc.getDeviceId(),
                        loc.getLatitude(),
                        loc.getLongitude(),
                        loc.getSource(),
                        loc.getAccuracyMeters(),
                        loc.getTimestamp()
                ));
            }

            // 4️⃣ Nenhuma localização válida
            System.out.printf("[WARN] Nenhuma localização válida encontrada. Device=%d%n", deviceId);
            return Optional.empty();

        } catch (Exception e) {
            System.err.printf("[ERROR] Falha ao obter localização do device=%d: %s%n", deviceId, e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }*/

    public Optional<LocationDTO> getCurrentLocation(Integer deviceId) {
        try {
            int gpsMaxAgeMinutes = 5;

            // 1) GPS recente
            var gpsRecentOpt = gpsRepo.findLatestValidGpsRecent(deviceId, gpsMaxAgeMinutes);
            if (gpsRecentOpt.isPresent()) {
                var gps = gpsRecentOpt.get();

                // opcional: manter cache, mas SOMENTE GPS
                cacheRepo.upsert(deviceId, gps.lat, gps.lon, "GPS", 10.0, "GPS:" + gps.id, gps.createdAt);

                return Optional.of(new LocationDTO(
                        gps.deviceId, gps.lat, gps.lon, "GPS", 10.0, gps.createdAt
                ));
            }

            // 2) Último GPS válido (mesmo antigo) + descrição explícita
            var gpsAnyOpt = gpsRepo.findLatestValidGps(deviceId);
            if (gpsAnyOpt.isPresent()) {
                var gps = gpsAnyOpt.get();

                var now = OffsetDateTime.now();
                long minutesOld = java.time.Duration.between(gps.createdAt, now).toMinutes();

                String desc = String.format("Última posição conhecida (GPS) — atualizada há %d min", minutesOld);

                // opcional: atualizar cache com GPS (mesmo antigo), se você quiser padronizar
                cacheRepo.upsert(deviceId, gps.lat, gps.lon, "GPS", 10.0, "GPS:" + gps.id, gps.createdAt);

                return Optional.of(new LocationDTO(
                        gps.deviceId, gps.lat, gps.lon, "GPS", 10.0, gps.createdAt, desc
                ));
            }

            // 3) Nunca teve GPS
            return Optional.empty(); // Controller devolve 204

        } catch (Exception e) {
            System.err.printf("[ERROR] Falha ao obter localização do device=%d: %s%n", deviceId, e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }


}
