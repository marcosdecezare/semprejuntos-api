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

    /**
     * Retorna todos os alarmes associados a um dispositivo, em ordem decrescente de data.
     */
    public List<AlarmDTO> findByDeviceId(Integer deviceId) {
        String sql = """
            SELECT 
                id,
                device_id,
                alarm_type,
                triggered_at
            FROM alarms
            WHERE device_id = ?
            ORDER BY triggered_at DESC
        """;

        return jdbcTemplate.query(sql, this::mapRow, deviceId);
    }

    /**
     * Remove todos os alarmes de um dispositivo específico.
     */
    public void deleteByDeviceId(Integer deviceId) {
        String sql = "DELETE FROM alarms WHERE device_id = ?";
        jdbcTemplate.update(sql, deviceId);
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
