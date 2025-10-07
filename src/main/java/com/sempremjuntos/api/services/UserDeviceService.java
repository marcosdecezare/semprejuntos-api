package com.sempremjuntos.api.services;

import com.sempremjuntos.api.entities.DeviceInfoDTO;
import com.sempremjuntos.api.repositories.UserDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserDeviceService {

    @Autowired
    private UserDeviceRepository repository;

    public List<DeviceInfoDTO> getDevicesByUserId(Integer userId) {
        return repository.findDevicesByUserId(userId);
    }
}
