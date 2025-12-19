package com.sempremjuntos.api.services;

import com.sempremjuntos.api.entities.TelemetryDTO;
import com.sempremjuntos.api.repositories.TelemetryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelemetryService {

    private final TelemetryRepository telemetryRepository;

    public TelemetryService(TelemetryRepository telemetryRepository) {
        this.telemetryRepository = telemetryRepository;
    }

    public List<TelemetryDTO> getLatestTelemetry(Integer deviceId, Integer limit) {
        int safeLimit = (limit == null || limit <= 0) ? 50 : Math.min(limit, 500);
        return telemetryRepository.findLatestByDeviceId(deviceId, safeLimit);
    }
}
