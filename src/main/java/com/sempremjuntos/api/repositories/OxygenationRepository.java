package com.sempremjuntos.api.repositories;

import com.sempremjuntos.api.entities.OxygenationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OxygenationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retorna todas as leituras válidas de oxigenação (SpO₂) de um dispositivo,
     * ordenadas do mais recente para o mais antigo.
     */
    public List<OxygenationDTO> findOxygenationByDeviceId(Integer deviceId) {
        String sql = """
            SELECT 
                device_id,
                spo2,
                created_at
            FROM semprejuntos.health_readings
            WHERE device_id = ?
              AND spo2 IS NOT NULL
              AND spo2 > 0
            ORDER BY created_at DESC
        """;

        return jdbcTemplate.query(sql, this::mapRow, deviceId);
    }

    private OxygenationDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new OxygenationDTO(
                rs.getInt("device_id"),
                rs.getInt("spo2"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
