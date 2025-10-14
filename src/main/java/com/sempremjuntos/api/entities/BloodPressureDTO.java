package com.sempremjuntos.api.entities;

import java.time.LocalDateTime;

/**
 * DTO que representa as leituras de pressão arterial de um dispositivo.
 */
public class BloodPressureDTO {

    private Integer deviceId;
    private Integer systolicBp;
    private Integer diastolicBp;
    private LocalDateTime createdAt;

    public BloodPressureDTO(Integer deviceId, Integer systolicBp, Integer diastolicBp, LocalDateTime createdAt) {
        this.deviceId = deviceId;
        this.systolicBp = systolicBp;
        this.diastolicBp = diastolicBp;
        this.createdAt = createdAt;
    }

    public Integer getDeviceId() { return deviceId; }
    public Integer getSystolicBp() { return systolicBp; }
    public Integer getDiastolicBp() { return diastolicBp; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
