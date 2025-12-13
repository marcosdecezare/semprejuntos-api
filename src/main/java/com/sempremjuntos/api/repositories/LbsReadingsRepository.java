package com.sempremjuntos.api.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public class LbsReadingsRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public static class LbsRow {
        public final int deviceId;
        public final int mcc, mnc, lac, cid;
        public final Integer signalStrength; // pode ser null
        public final OffsetDateTime createdAt;
        public LbsRow(int deviceId, int mcc, int mnc, int lac, int cid, Integer signalStrength, OffsetDateTime createdAt) {
            this.deviceId = deviceId; this.mcc = mcc; this.mnc = mnc; this.lac = lac; this.cid = cid;
            this.signalStrength = signalStrength; this.createdAt = createdAt;
        }
        public String signature() { return "LBS:%d-%d-%d-%d".formatted(mcc, mnc, lac, cid); }
    }

    public Optional<LbsRow> findLatestLbs(Integer deviceId) {
        String sql = """
          SELECT device_id, mcc, mnc, lac, cid, signal_strength, created_at
          FROM lbs_readings
          WHERE device_id = ?
          ORDER BY created_at DESC
          LIMIT 1
        """;
        return jdbc.query(sql, rs -> {
            if (rs.next()) {
                return Optional.of(new LbsRow(
                        rs.getInt("device_id"),
                        rs.getInt("mcc"),
                        rs.getInt("mnc"),
                        rs.getInt("lac"),
                        rs.getInt("cid"),
                        (Integer) rs.getObject("signal_strength"),
                        rs.getObject("created_at", OffsetDateTime.class)
                ));
            }
            return Optional.empty();
        }, deviceId);
    }
}
