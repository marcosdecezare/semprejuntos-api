package com.semprejuntos.api.services;

import com.semprejuntos.api.entities.DeviceStatus;
import com.semprejuntos.api.repositories.DeviceStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceStatusService {

    @Autowired
    private DeviceStatusRepository repository;

    public DeviceStatus getLatestStatusByImei(String imei) {
        return repository.findLatestByImei(imei);
    }
}
