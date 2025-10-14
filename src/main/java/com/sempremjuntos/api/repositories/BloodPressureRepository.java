package com.sempremjuntos.api.repositories;

import com.sempremjuntos.api.entities.BloodPressureDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BloodPressureRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retorna todas as leituras válidas de pressão arterial de um dispositivo,
     * ordenadas do mais recente para o mais antigo.
     * São consideradas válidas as leituras onde ambos os valores (sistólico e diastólico) não são nulos.
     */
    public List<BloodPressureDTO> findBloodPressureByDeviceId(Integer deviceId) {
        String sql = """
            SELECT 
                device_id,
                systolic_bp,
                diastolic_bp,
                created_at
            FROM semprejuntos.health_readings
            WHERE device_id = ?
              AND systolic_bp IS NOT NULL
              AND diastolic_bp IS NOT NULL
            ORDER BY created_at DESC
        """;

        return jdbcTemplate.query(sql, this::mapRow, deviceId);
    }

    private BloodPressureDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BloodPressureDTO(
                rs.getInt("device_id"),
                rs.getInt("systolic_bp"),
                rs.getInt("diastolic_bp"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
