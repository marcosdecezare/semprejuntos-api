package com.sempremjuntos.api.services;

import com.sempremjuntos.api.repositories.CommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Camada de serviço para envio de comandos do servidor para o relógio.
 */
@Service
public class CommandService {

    @Autowired
    private CommandRepository repository;

    /**
     * Registra um comando na tabela de buffer.
     *
     * @param deviceId    id do device
     * @param commandType código do comando
     */
    public void sendCommand(Integer deviceId, String commandType) {
        // aqui podemos no futuro validar se o device existe, se o comando é permitido, etc.
        repository.insertPendingCommand(deviceId, commandType);
    }
}
