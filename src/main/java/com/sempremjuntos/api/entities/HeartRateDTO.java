package com.sempremjuntos.api.entities;

import java.time.LocalDateTime;

/**
 * DTO que representa as leituras de batimentos cardíacos de um dispositivo.
 */
public class HeartRateDTO {

    private Integer deviceId;
    private Integer heartRate;
    private LocalDateTime createdAt;

    public HeartRateDTO(Integer deviceId, Integer heartRate, LocalDateTime createdAt) {
        this.deviceId = deviceId;
        this.heartRate = heartRate;
        this.createdAt = createdAt;
    }

    public Integer getDeviceId() { return deviceId; }
    public Integer getHeartRate() { return heartRate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
