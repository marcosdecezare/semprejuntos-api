package com.sempremjuntos.api.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public class LocationReadingsRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public static class GpsRow {
        public final int id;
        public final int deviceId;
        public final double lat;
        public final double lon;
        public final OffsetDateTime createdAt;
        public GpsRow(int id, int deviceId, double lat, double lon, OffsetDateTime createdAt) {
            this.id = id; this.deviceId = deviceId; this.lat = lat; this.lon = lon; this.createdAt = createdAt;
        }
    }

    public Optional<GpsRow> findLatestValidGps(Integer deviceId) {
        String sql = """
          SELECT id, device_id, latitude, longitude, created_at
          FROM location_readings
          WHERE device_id = ?
            AND latitude IS NOT NULL AND longitude IS NOT NULL
            AND latitude <> 0 AND longitude <> 0
          ORDER BY created_at DESC
          LIMIT 1
        """;
        return jdbc.query(sql, rs -> {
            if (rs.next()) {
                return Optional.of(new GpsRow(
                        rs.getInt("id"),
                        rs.getInt("device_id"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"),
                        rs.getObject("created_at", OffsetDateTime.class)
                ));
            }
            return Optional.empty();
        }, deviceId);
    }

    public Optional<GpsRow> findLatestValidGpsRecent(Integer deviceId, int maxAgeMinutes) {
        String sql = """
      SELECT id, device_id, latitude, longitude, created_at
      FROM location_readings
      WHERE device_id = ?
        AND created_at >= (now() - make_interval(mins => ?))
        AND latitude IS NOT NULL AND longitude IS NOT NULL
        AND latitude <> 0 AND longitude <> 0
        AND latitude BETWEEN -90 AND 90
        AND longitude BETWEEN -180 AND 180
      ORDER BY created_at DESC
      LIMIT 1
    """;

        return jdbc.query(sql, rs -> {
            if (rs.next()) {
                return Optional.of(new GpsRow(
                        rs.getInt("id"),
                        rs.getInt("device_id"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"),
                        rs.getObject("created_at", OffsetDateTime.class)
                ));
            }
            return Optional.empty();
        }, deviceId, maxAgeMinutes);
    }



}
