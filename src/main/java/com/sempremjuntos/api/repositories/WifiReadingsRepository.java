package com.sempremjuntos.api.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WifiReadingsRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public static class WifiRow {
        public final int deviceId;
        public final String ssid;
        public final String macAddress;
        public final Integer signalStrength;
        public final OffsetDateTime createdAt;

        public WifiRow(int deviceId,
                       String ssid,
                       String macAddress,
                       Integer signalStrength,
                       OffsetDateTime createdAt) {
            this.deviceId = deviceId;
            this.ssid = ssid;
            this.macAddress = macAddress;
            this.signalStrength = signalStrength;
            this.createdAt = createdAt;
        }
    }

    /**
     * Retorna as últimas redes Wi-Fi vistas pelo dispositivo,
     * ordenadas por força de sinal (mais forte primeiro) e depois por data.
     */
    public List<WifiRow> findRecentWifi(Integer deviceId, int limit) {
        String sql = """
            SELECT device_id, ssid, mac_address, signal_strength, created_at
            FROM wifi_readings
            WHERE device_id = ?
            ORDER BY created_at DESC
            LIMIT ?
        """;

        return jdbc.query(sql, rs -> {
            List<WifiRow> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new WifiRow(
                        rs.getInt("device_id"),
                        rs.getString("ssid"),
                        rs.getString("mac_address"),
                        (Integer) rs.getObject("signal_strength"),
                        rs.getObject("created_at", OffsetDateTime.class)
                ));
            }
            return list;
        }, deviceId, limit);
    }
}
