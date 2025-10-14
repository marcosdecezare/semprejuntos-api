package com.sempremjuntos.api.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO que representa as leituras de temperatura corporal de um dispositivo.
 */
public class HealthReadingDTO {

    private Integer deviceId;
    private BigDecimal bodyTemperature;
    private LocalDateTime createdAt;

    public HealthReadingDTO(Integer deviceId, BigDecimal bodyTemperature, LocalDateTime createdAt) {
        this.deviceId = deviceId;
        this.bodyTemperature = bodyTemperature;
        this.createdAt = createdAt;
    }

    public Integer getDeviceId() { return deviceId; }
    public BigDecimal getBodyTemperature() { return bodyTemperature; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
