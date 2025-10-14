package com.sempremjuntos.api.entities;

import java.time.LocalDateTime;

/**
 * DTO que representa as leituras de oxigenação (SpO₂) de um dispositivo.
 */
public class OxygenationDTO {

    private Integer deviceId;
    private Integer spo2;
    private LocalDateTime createdAt;

    public OxygenationDTO(Integer deviceId, Integer spo2, LocalDateTime createdAt) {
        this.deviceId = deviceId;
        this.spo2 = spo2;
        this.createdAt = createdAt;
    }

    public Integer getDeviceId() { return deviceId; }
    public Integer getSpo2() { return spo2; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
