package com.sempremjuntos.api.entities;

import java.time.LocalDateTime;

public class DeviceStatusDTO {
    private Integer deviceId;
    private Integer batteryLevel;
    private Integer gsmSignal;
    private LocalDateTime lastUpdate;

    public DeviceStatusDTO(Integer deviceId, Integer batteryLevel, Integer gsmSignal, LocalDateTime lastUpdate) {
        this.deviceId = deviceId;
        this.batteryLevel = batteryLevel;
        this.gsmSignal = gsmSignal;
        this.lastUpdate = lastUpdate;
    }

    public Integer getDeviceId() { return deviceId; }
    public Integer getBatteryLevel() { return batteryLevel; }
    public Integer getGsmSignal() { return gsmSignal; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
}
