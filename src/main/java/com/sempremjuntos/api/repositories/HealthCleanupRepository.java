package com.sempremjuntos.api.repositories;

import com.sempremjuntos.api.entities.CleanupType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Repositório responsável por excluir registros específicos da tabela semprejuntos.health_readings,
 * conforme o tipo de limpeza solicitado.
 */
@Repository
public class HealthCleanupRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Executa a limpeza conforme o tipo.
     *
     * @param deviceId id do dispositivo
     * @param type     tipo de limpeza (HEART, SYS_DIA, SPO, TEMP, STEP, ALL)
     * @return número de linhas afetadas
     */
    public int clean(Integer deviceId, CleanupType type) {
        return switch (type) {
            case HEART -> jdbcTemplate.update("""
                    DELETE FROM semprejuntos.health_readings
                    WHERE device_id = ?
                      AND heart_rate IS NOT NULL
                """, deviceId);

            case SYS_DIA -> jdbcTemplate.update("""
                    DELETE FROM semprejuntos.health_readings
                    WHERE device_id = ?
                      AND (systolic_bp IS NOT NULL OR diastolic_bp IS NOT NULL)
                """, deviceId);

            case SPO -> jdbcTemplate.update("""
                    DELETE FROM semprejuntos.health_readings
                    WHERE device_id = ?
                      AND spo2 IS NOT NULL
                """, deviceId);

            case TEMP -> jdbcTemplate.update("""
                    DELETE FROM semprejuntos.health_readings
                    WHERE device_id = ?
                      AND body_temperature IS NOT NULL
                """, deviceId);

            case STEP -> jdbcTemplate.update("""
                    DELETE FROM semprejuntos.health_readings
                    WHERE device_id = ?
                      AND step_count IS NOT NULL
                """, deviceId);

            case ALL -> jdbcTemplate.update("""
                    DELETE FROM semprejuntos.health_readings
                    WHERE device_id = ?
                """, deviceId);
        };
    }
}
