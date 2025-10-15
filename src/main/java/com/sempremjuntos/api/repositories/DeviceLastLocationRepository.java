package com.sempremjuntos.api.repositories;

import com.sempremjuntos.api.entities.LocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Cache da última localização resolvida (GPS/LBS/WIFI).
 * - Nunca grava nem retorna (0,0)
 * - Converte NUMERIC (BigDecimal) para Double ao ler accuracy_m
 */
@Repository
public class DeviceLastLocationRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public Optional<LocationDTO> findByDeviceId(Integer deviceId) {
        String sql = """
            SELECT 
                device_id,
                latitude,
                longitude,
                source,
                accuracy_m,
                resolved_at
            FROM semprejuntos.device_last_location
            WHERE device_id = ?
              AND latitude IS NOT NULL
              AND longitude IS NOT NULL
              AND latitude <> 0
              AND longitude <> 0
        """;

        return jdbc.query(sql, rs -> {
            if (rs.next()) {
                BigDecimal acc = (BigDecimal) rs.getObject("accuracy_m");
                Double accuracy = acc != null ? acc.doubleValue() : null;

                return Optional.of(new LocationDTO(
                        rs.getInt("device_id"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"),
                        rs.getString("source"),
                        accuracy,
                        rs.getObject("resolved_at", OffsetDateTime.class)
                ));
            }
            return Optional.empty();
        }, deviceId);
    }

    public Optional<String> findSignature(Integer deviceId) {
        String sql = """
            SELECT signature
            FROM semprejuntos.device_last_location
            WHERE device_id = ?
        """;
        return jdbc.query(sql, rs -> rs.next() ? Optional.ofNullable(rs.getString("signature")) : Optional.empty(), deviceId);
    }

    public void upsert(Integer deviceId,
                       double latitude,
                       double longitude,
                       String source,
                       Double accuracyMeters,
                       String signature,
                       OffsetDateTime resolvedAt) {

        if (latitude == 0.0 || longitude == 0.0) {
            System.out.printf("[WARN] Ignorando gravação inválida (0,0) para device_id=%d%n", deviceId);
            return;
        }

        String sql = """
            INSERT INTO semprejuntos.device_last_location
                (device_id, latitude, longitude, source, accuracy_m, signature, resolved_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, now())
            ON CONFLICT (device_id) DO UPDATE SET
                latitude     = EXCLUDED.latitude,
                longitude    = EXCLUDED.longitude,
                source       = EXCLUDED.source,
                accuracy_m   = EXCLUDED.accuracy_m,
                signature    = EXCLUDED.signature,
                resolved_at  = EXCLUDED.resolved_at,
                updated_at   = now()
        """;

        jdbc.update(sql, deviceId, latitude, longitude, source, accuracyMeters, signature, resolvedAt);
    }

    public int cleanInvalidRecords() {
        String sql = """
            DELETE FROM semprejuntos.device_last_location
            WHERE latitude = 0 OR longitude = 0
        """;
        return jdbc.update(sql);
    }
}
