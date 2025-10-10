package com.sempremjuntos.api.services;

import com.sempremjuntos.api.entities.DeviceStatusDTO;
import com.sempremjuntos.api.repositories.DeviceStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceStatusService {

    @Autowired
    private DeviceStatusRepository repository;

    public Optional<DeviceStatusDTO> getLatestStatus(Integer deviceId) {
        return repository.findLatestByDeviceId(deviceId);
    }
}
