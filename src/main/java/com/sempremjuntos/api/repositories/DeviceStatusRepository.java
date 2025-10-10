package com.sempremjuntos.api.repositories;

import com.sempremjuntos.api.entities.DeviceStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class DeviceStatusRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<DeviceStatusDTO> findLatestByDeviceId(Integer deviceId) {
        String sql = """
            SELECT 
                ds.device_id,
                ds.battery_level,
                ds.gsm_signal,
                ds.last_update
            FROM semprejuntos.device_status ds
            WHERE ds.device_id = ?
            ORDER BY ds.last_update DESC
            LIMIT 1
        """;

        return jdbcTemplate.query(sql, new Object[]{deviceId}, this::mapRow)
                .stream()
                .findFirst();
    }

    private DeviceStatusDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DeviceStatusDTO(
                rs.getInt("device_id"),
                rs.getInt("battery_level"),
                rs.getInt("gsm_signal"),
                rs.getTimestamp("last_update").toLocalDateTime()
        );
    }
}
