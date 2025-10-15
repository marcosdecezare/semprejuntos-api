package com.sempremjuntos.api.repositories;

import com.sempremjuntos.api.entities.HeartRateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class HeartRateRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retorna todas as leituras válidas de batimentos cardíacos de um dispositivo,
     * ordenadas do mais recente para o mais antigo.
     */
    public List<HeartRateDTO> findHeartRateByDeviceId(Integer deviceId) {
        String sql = """
            SELECT 
                device_id,
                heart_rate,
                created_at
            FROM semprejuntos.health_readings
            WHERE device_id = ?
              AND heart_rate IS NOT NULL
              AND heart_rate > 0
            ORDER BY created_at DESC
        """;

        return jdbcTemplate.query(sql, this::mapRow, deviceId);
    }

    private HeartRateDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new HeartRateDTO(
                rs.getInt("device_id"),
                rs.getInt("heart_rate"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
