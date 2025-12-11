package com.sempremjuntos.api.services;

import com.sempremjuntos.api.entities.LocationDTO;
import com.sempremjuntos.api.repositories.DeviceLastLocationRepository;
import com.sempremjuntos.api.repositories.LbsReadingsRepository;
import com.sempremjuntos.api.repositories.LocationReadingsRepository;
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

    /**
     * Obtém a localização mais recente e válida do dispositivo.
     * Fluxo: GPS válido → LBS (Google) → Cache.
     */
    public Optional<LocationDTO> getCurrentLocation(Integer deviceId) {
        try {
            // 1️⃣ GPS válido (latitude/longitude ≠ 0), mas desconsiderando GPS muito antigo
            var gpsOpt = gpsRepo.findLatestValidGps(deviceId);
            if (gpsOpt.isPresent()) {
                var gps = gpsOpt.get();

                // Limite de "frescura" do GPS: 5 minutos
                var now = OffsetDateTime.now();
                long gpsMaxAgeMinutes = 5L;

                if (gps.createdAt.isBefore(now.minusMinutes(gpsMaxAgeMinutes))) {
                    // GPS é válido, mas muito antigo → não usa, cai para LBS
                    System.out.printf(
                            "[GPS] Último GPS válido muito antigo para device=%d. created_at=%s, now=%s, limite=%d min%n",
                            deviceId, gps.createdAt, now, gpsMaxAgeMinutes
                    );
                } else {
                    // GPS ainda é recente → mantém comportamento atual
                    var sig = "GPS:" + gps.id;
                    var lastSig = cacheRepo.findSignature(deviceId).orElse(null);

                    if (!sig.equals(lastSig)) {
                        System.out.printf("[GPS] Novo registro detectado. Device=%d | ID=%d | lat=%.7f | lon=%.7f | created_at=%s%n",
                                gps.deviceId, gps.id, gps.lat, gps.lon, gps.createdAt);
                        cacheRepo.upsert(deviceId, gps.lat, gps.lon, "GPS", 10.0, sig, gps.createdAt);
                    } else {
                        System.out.printf("[GPS] Nenhuma atualização. Device=%d (assinatura igual) | last_created_at=%s%n",
                                deviceId, gps.createdAt);
                    }

                    var cached = cacheRepo.findByDeviceId(deviceId);
                    if (cached.isPresent()) {
                        var loc = cached.get();
                        System.out.printf("[GPS] Retornando coordenadas válidas do cache. Device=%d | resolved_at=%s%n",
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
                }
            } else {
                System.out.printf("[GPS] Nenhum GPS válido encontrado para device=%d%n", deviceId);
            }

            // 2️⃣ Fallback LBS (usa Google Geolocation API) — permanece igual
            var lbsOpt = lbsRepo.findLatestLbs(deviceId);
            if (lbsOpt.isPresent()) {
                var lbs = lbsOpt.get();
                var sig = lbs.signature();
                var lastSig = cacheRepo.findSignature(deviceId).orElse(null);

                if (!sig.equals(lastSig)) {
                    System.out.printf("[LBS] Tentando resolver via Google. Device=%d | %s | created_at=%s%n",
                            deviceId, sig, lbs.createdAt);
                    var resolved = geo.resolveLbs(lbs.mcc, lbs.mnc, lbs.lac, lbs.cid, lbs.signalStrength);
                    if (resolved.isPresent()) {
                        var p = resolved.get();
                        if (p.latitude() != 0.0 && p.longitude() != 0.0) {
                            System.out.printf("[LBS-GOOGLE] Resolved: lat=%.7f lon=%.7f acc=%.1fm%n",
                                    p.latitude(), p.longitude(), p.accuracyMeters());
                            cacheRepo.upsert(deviceId, p.latitude(), p.longitude(),
                                    "LBS", p.accuracyMeters(), sig, OffsetDateTime.now());
                        } else {
                            System.out.printf("[LBS-GOOGLE] Coordenadas inválidas (0,0). Ignorando.%n");
                        }
                    } else {
                        System.out.printf("[LBS-GOOGLE] Nenhuma coordenada retornada. Device=%d%n", deviceId);
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
    }


}
