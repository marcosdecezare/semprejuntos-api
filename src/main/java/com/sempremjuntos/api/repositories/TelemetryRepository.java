package com.sempremjuntos.api.repositories;

import com.sempremjuntos.api.entities.TelemetryDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class TelemetryRepository {

    private final JdbcTemplate jdbcTemplate;

    // Mesmo padrão do LocationDTO: dd/MM/yyyy HH:mm
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public TelemetryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TelemetryDTO> findLatestByDeviceId(Integer deviceId, int limit) {
        String sql = """
            SELECT
                id,
                device_id,
                packet_type,
                raw_payload,
                created_at,
                client_ip
            FROM telemetry
            WHERE device_id = ?
            ORDER BY id DESC
            LIMIT ?
        """;

        return jdbcTemplate.query(sql, new Object[]{deviceId, limit}, (rs, rowNum) -> {

            // created_at no banco é "timestamp" (sem timezone).
            // Portanto: NÃO converter para OffsetDateTime (isso gera defasagem).
            LocalDateTime createdAt = rs.getObject("created_at", LocalDateTime.class);
            String createdAtFormatted = (createdAt != null) ? createdAt.format(FORMATTER) : null;

            // Importante: este construtor assume que TelemetryDTO possui o construtor com String já formatada,
            // assim como seu LocationDTO.
            return new TelemetryDTO(
                    rs.getInt("id"),
                    rs.getInt("device_id"),
                    rs.getString("packet_type"),
                    rs.getString("raw_payload"),
                    createdAtFormatted,
                    rs.getString("client_ip")
            );
        });
    }
}
