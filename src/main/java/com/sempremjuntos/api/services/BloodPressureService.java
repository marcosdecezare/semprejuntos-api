package com.sempremjuntos.api.services;

import com.sempremjuntos.api.entities.BloodPressureDTO;
import com.sempremjuntos.api.repositories.BloodPressureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodPressureService {

    @Autowired
    private BloodPressureRepository repository;

    /**
     * Retorna as leituras válidas de pressão arterial de um dispositivo.
     */
    public List<BloodPressureDTO> getBloodPressureByDevice(Integer deviceId) {
        return repository.findBloodPressureByDeviceId(deviceId);
    }
}
