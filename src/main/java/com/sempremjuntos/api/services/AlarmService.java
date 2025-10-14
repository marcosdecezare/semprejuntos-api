package com.sempremjuntos.api.services;

import com.sempremjuntos.api.entities.AlarmDTO;
import com.sempremjuntos.api.repositories.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmService {

    @Autowired
    private AlarmRepository repository;

    /**
     * Retorna a lista de alarmes de um dispositivo.
     */
    public List<AlarmDTO> getAlarmsByDevice(Integer deviceId) {
        return repository.findByDeviceId(deviceId);
    }

    /**
     * Remove todos os alarmes de um dispositivo específico.
     */
    public void clearAlarmsByDevice(Integer deviceId) {
        repository.deleteByDeviceId(deviceId);
    }
}
