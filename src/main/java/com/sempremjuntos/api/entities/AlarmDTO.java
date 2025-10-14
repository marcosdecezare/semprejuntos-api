package com.sempremjuntos.api.entities;

import java.time.LocalDateTime;

public class AlarmDTO {
    private Integer id;
    private Integer deviceId;
    private String alarmType;
    private LocalDateTime triggeredAt;

    public AlarmDTO(Integer id, Integer deviceId, String alarmType, LocalDateTime triggeredAt) {
        this.id = id;
        this.deviceId = deviceId;
        this.alarmType = alarmType;
        this.triggeredAt = triggeredAt;
    }

    public Integer getId() { return id; }
    public Integer getDeviceId() { return deviceId; }
    public String getAlarmType() { return alarmType; }
    public LocalDateTime getTriggeredAt() { return triggeredAt; }
}
