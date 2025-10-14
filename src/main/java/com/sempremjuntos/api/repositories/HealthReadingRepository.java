package com.sempremjuntos.api.repositories;

import com.sempremjuntos.api.entities.HealthReadingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class HealthReadingRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retorna todas as leituras de temperatura corporal válidas de um dispositivo,
     * em ordem decrescente de data/hora.
     */
    public List<HealthReadingDTO> findBodyTemperatureByDeviceId(Integer deviceId) {
        String sql = """
            SELECT 
                device_id,
                body_temperature,
                created_at
            FROM semprejuntos.health_readings
            WHERE device_id = ?
              AND body_temperature IS NOT NULL
            ORDER BY created_at DESC
        """;

        return jdbcTemplate.query(sql, this::mapRow, deviceId);
    }

    private HealthReadingDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new HealthReadingDTO(
                rs.getInt("device_id"),
                rs.getBigDecimal("body_temperature"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
