package com.sempremjuntos.api.repositories;

import com.sempremjuntos.api.entities.LocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Repositório responsável por armazenar e recuperar a última localização resolvida
 * (GPS, LBS ou Wi-Fi) de cada dispositivo.
 *
 * Inclui:
 * - Filtro automático de coordenadas inválidas (latitude/longitude = 0)
 * - Cache local para evitar chamadas desnecessárias ao serviço externo
 */
@Repository
public class DeviceLastLocationRepository {

    @Autowired
    private JdbcTemplate jdbc;

    /**
     * Retorna a última localização válida (latitude/longitude diferentes de zero)
     * armazenada no cache para o device informado.
     */
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
                return Optional.of(new LocationDTO(
                        rs.getInt("device_id"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"),
                        rs.getString("source"),
                        (Double) rs.getObject("accuracy_m"),
                        rs.getObject("resolved_at", OffsetDateTime.class)
                ));
            }
            return Optional.empty();
        }, deviceId);
    }

    /**
     * Retorna apenas a assinatura ("signature") do último cache de localização do device.
     * Exemplo: GPS:12345 ou LBS:724-5-4501-53211
     */
    public Optional<String> findSignature(Integer deviceId) {
        String sql = """
            SELECT signature
            FROM semprejuntos.device_last_location
            WHERE device_id = ?
        """;

        return jdbc.query(sql, rs -> {
            if (rs.next()) {
                return Optional.ofNullable(rs.getString("signature"));
            }
            return Optional.empty();
        }, deviceId);
    }

    /**
     * Atualiza (ou insere) a última localização resolvida do dispositivo.
     * Evita gravar coordenadas inválidas (latitude/longitude = 0).
     */
    public void upsert(Integer deviceId,
                       double latitude,
                       double longitude,
                       String source,
                       Double accuracyMeters,
                       String signature,
                       OffsetDateTime resolvedAt) {

        // 🚫 Ignora gravações inválidas
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

    /**
     * Remove do cache qualquer registro inválido (0,0).
     * Pode ser usado para limpeza periódica via cron ou manutenção manual.
     */
    public int cleanInvalidRecords() {
        String sql = """
            DELETE FROM semprejuntos.device_last_location
            WHERE latitude = 0 OR longitude = 0
        """;
        return jdbc.update(sql);
    }
}
