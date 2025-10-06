package com.semprejuntos.api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "device_status", schema = "semprejuntos")
public class DeviceStatus {

    @Id
    @Column(name = "device_id")
    private Integer deviceId;

    @Column(name = "battery_level")
    private Integer batteryLevel;

    @Column(name = "gsm_signal")
    private Integer gsmSignal;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
