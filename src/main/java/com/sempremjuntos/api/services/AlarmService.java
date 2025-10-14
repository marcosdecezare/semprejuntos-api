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

    public List<AlarmDTO> getAlarmsByDevice(Integer deviceId) {
        return repository.findByDeviceId(deviceId);
    }
}
