package com.sempremjuntos.api.services;

import com.sempremjuntos.api.entities.DeviceConfigDTO;
import com.sempremjuntos.api.repositories.DeviceConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import com.sempremjuntos.api.entities.DeviceConfigUpdateRequest;
import java.util.Optional;

@Service
public class DeviceConfigService {

    @Autowired
    private DeviceConfigRepository repository;

    public Optional<DeviceConfigDTO> getConfig(Integer deviceId) {
        return repository.findConfigByDeviceId(deviceId);
    }

    public Optional<DeviceConfigDTO> updateConfig(Integer deviceId, DeviceConfigUpdateRequest req) {
        int rows = repository.updateConfig(deviceId, req);

        if (rows == 0) {
            return Optional.empty();
        }

        // Retorna a configuração atualizada
        return repository.findConfigByDeviceId(deviceId);
    }


}
