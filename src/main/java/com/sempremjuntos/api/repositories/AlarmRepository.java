package com.sempremjuntos.api.repositories;

import com.sempremjuntos.api.entities.AlarmDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AlarmRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<AlarmDTO> findByDeviceId(Integer deviceId) {
        String sql = """
            SELECT 
                id,
                device_id,
                alarm_type,
                triggered_at
            FROM semprejuntos.alarms
            WHERE device_id = ?
            ORDER BY triggered_at DESC
        """;

        return jdbcTemplate.query(sql, this::mapRow, deviceId);
    }

    private AlarmDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AlarmDTO(
                rs.getInt("id"),
                rs.getInt("device_id"),
                rs.getString("alarm_type"),
                rs.getTimestamp("triggered_at").toLocalDateTime()
        );
    }
}
