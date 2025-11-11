package com.sempremjuntos.api.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Repositório responsável por inserir comandos na fila (command_buffer).
 */
@Repository
public class CommandRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Insere um comando pendente na tabela semprejuntos.command_buffer.
     *
     * @param deviceId    id do dispositivo
     * @param commandType tipo do comando (ex.: BPXT, REBOOT, SOSCFG, etc)
     * @return número de linhas afetadas (1 se inseriu)
     */
    public int insertPendingCommand(Integer deviceId, String commandType) {
        String sql = """
            INSERT INTO semprejuntos.command_buffer (device_id, command_type, status)
            VALUES (?, ?, 'PENDING')
        """;
        return jdbcTemplate.update(sql, deviceId, commandType);
    }
}
