package com.sempremjuntos.api.services;

import com.sempremjuntos.api.entities.OxygenationDTO;
import com.sempremjuntos.api.repositories.OxygenationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OxygenationService {

    @Autowired
    private OxygenationRepository repository;

    /**
     * Retorna as leituras válidas de oxigenação (SpO₂) de um dispositivo.
     */
    public List<OxygenationDTO> getOxygenationByDevice(Integer deviceId) {
        return repository.findOxygenationByDeviceId(deviceId);
    }
}
