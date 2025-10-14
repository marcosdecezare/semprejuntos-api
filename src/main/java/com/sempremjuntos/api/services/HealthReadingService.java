package com.sempremjuntos.api.services;

import com.sempremjuntos.api.entities.HealthReadingDTO;
import com.sempremjuntos.api.repositories.HealthReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthReadingService {

    @Autowired
    private HealthReadingRepository repository;

    /**
     * Retorna as leituras de temperatura corporal válidas de um dispositivo.
     */
    public List<HealthReadingDTO> getBodyTemperatureByDevice(Integer deviceId) {
        return repository.findBodyTemperatureByDeviceId(deviceId);
    }
}
