package com.sempremjuntos.api.services;

import com.sempremjuntos.api.entities.HeartRateDTO;
import com.sempremjuntos.api.repositories.HeartRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeartRateService {

    @Autowired
    private HeartRateRepository repository;

    /**
     * Retorna as leituras válidas de batimentos cardíacos de um dispositivo.
     */
    public List<HeartRateDTO> getHeartRateByDevice(Integer deviceId) {
        return repository.findHeartRateByDeviceId(deviceId);
    }
}
