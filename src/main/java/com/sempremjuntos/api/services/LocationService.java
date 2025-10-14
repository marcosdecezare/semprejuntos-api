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
 * Serviço responsável por obter a localização atual do dispositivo.
 * Prioriza GPS, faz fallback via LBS (resolução com Mozilla Location Service)
 * e utiliza cache local em device_last_location.
 */
@Service
public class LocationService {

    @Autowired private LocationReadingsRepository gpsRepo;
    @Autowired private LbsReadingsRepository lbsRepo;
    @Autowired private DeviceLastLocationRepository cacheRepo;
    @Autowired private GeoLocationResolverService geo;

    /**
     * Obtém a localização mais recente e válida do dispositivo.
     * Fluxo: GPS válido → LBS (Mozilla) → Cache.
     */
    public Optional<LocationDTO> getCurrentLocation(Integer deviceId) {
        try {
            // 1️⃣ GPS válido (latitude/longitude ≠ 0)
            var gpsOpt = gpsRepo.findLatestValidGps(deviceId);
            if (gpsOpt.isPresent()) {
                var gps = gpsOpt.get();
                var sig = "GPS:" + gps.id;
                var lastSig = cacheRepo.findSignature(deviceId).orElse(null);

                if (!sig.equals(lastSig)) {
                    System.out.printf("[GPS] Novo registro detectado. Device=%d | ID=%d | lat=%.7f | lon=%.7f%n",
                            gps.deviceId, gps.id, gps.lat, gps.lon);
                    cacheRepo.upsert(deviceId, gps.lat, gps.lon, "GPS", null, sig, gps.createdAt);
                } else {
                    System.out.printf("[GPS] Nenhuma atualização. Device=%d (assinatura igual)%n", deviceId);
                }

                var cached = cacheRepo.findByDeviceId(deviceId);
                if (cached.isPresent()) {
                    System.out.printf("[GPS] Retornando coordenadas válidas do cache. Device=%d%n", deviceId);
                    return cached;
                }
            } else {
                System.out.printf("[GPS] Nenhum GPS válido encontrado para device=%d%n", deviceId);
            }

            // 2️⃣ Fallback LBS (usa Mozilla API)
            var lbsOpt = lbsRepo.findLatestLbs(deviceId);
            if (lbsOpt.isPresent()) {
                var lbs = lbsOpt.get();
                var sig = lbs.signature();
                var lastSig = cacheRepo.findSignature(deviceId).orElse(null);

                if (!sig.equals(lastSig)) {
                    System.out.printf("[LBS] Tentando resolver via Mozilla. Device=%d | %s%n", deviceId, sig);
                    var resolved = geo.resolveLbs(lbs.mcc, lbs.mnc, lbs.lac, lbs.cid, lbs.signalStrength);
                    if (resolved.isPresent()) {
                        var p = resolved.get();
                        if (p.latitude() != 0.0 && p.longitude() != 0.0) {
                            System.out.printf("[LBS] Mozilla retornou lat=%.7f lon=%.7f acc=%.1fm%n",
                                    p.latitude(), p.longitude(), p.accuracyMeters());
                            cacheRepo.upsert(deviceId, p.latitude(), p.longitude(),
                                    "LBS", p.accuracyMeters(), sig, OffsetDateTime.now());
                        } else {
                            System.out.printf("[LBS] Mozilla retornou coordenadas inválidas (0,0). Ignorando.%n");
                        }
                    } else {
                        System.out.printf("[LBS] Mozilla não retornou localização. Device=%d%n", deviceId);
                    }
                } else {
                    System.out.printf("[LBS] Assinatura igual. Usando cache atual. Device=%d%n", deviceId);
                }

                var cached = cacheRepo.findByDeviceId(deviceId);
                if (cached.isPresent()) {
                    System.out.printf("[LBS] Retornando coordenadas válidas do cache. Device=%d%n", deviceId);
                    return cached;
                }
            } else {
                System.out.printf("[LBS] Nenhum registro LBS encontrado para device=%d%n", deviceId);
            }

            // 3️⃣ Último cache válido (quando GPS/LBS não retornam nada)
            var cached = cacheRepo.findByDeviceId(deviceId);
            if (cached.isPresent()) {
                System.out.printf("[CACHE] Retornando última posição conhecida. Device=%d | Fonte=%s%n",
                        deviceId, cached.get().getSource());
                return cached;
            }

            // 4️⃣ Sem nada válido
            System.out.printf("[WARN] Nenhuma localização válida encontrada. Device=%d%n", deviceId);
            return Optional.empty();

        } catch (Exception e) {
            System.err.printf("[ERROR] Falha ao obter localização do device=%d: %s%n", deviceId, e.getMessage());
            return Optional.empty();
        }
    }
}
