package com.sempremjuntos.api.entities;

/**
 * Request para limpeza de health_readings.
 * type deve ser um dentre: HEART, SYS_DIA, SPO, TEMP, STEP, ALL
 */
public class HealthCleanupRequestDTO {

    private Integer deviceId;
    private String type; // HEART | SYS_DIA | SPO | TEMP | STEP | ALL

    public HealthCleanupRequestDTO() {}

    public HealthCleanupRequestDTO(Integer deviceId, String type) {
        this.deviceId = deviceId;
        this.type = type;
    }

    public Integer getDeviceId() { return deviceId; }
    public void setDeviceId(Integer deviceId) { this.deviceId = deviceId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
