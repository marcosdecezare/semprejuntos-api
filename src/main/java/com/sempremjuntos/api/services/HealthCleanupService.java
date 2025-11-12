package com.sempremjuntos.api.services;

import com.sempremjuntos.api.entities.CleanupType;
import com.sempremjuntos.api.repositories.HealthCleanupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Serviço para executar limpezas específicas da tabela health_readings.
 */
@Service
public class HealthCleanupService {

    @Autowired
    private HealthCleanupRepository repository;

    public int clean(Integer deviceId, CleanupType type) {
        return repository.clean(deviceId, type);
    }
}
