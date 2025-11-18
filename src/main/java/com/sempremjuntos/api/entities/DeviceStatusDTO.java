package com.sempremjuntos.api.entities;

import java.time.LocalDateTime;

public class DeviceStatusDTO {
    private Integer deviceId;
    private Integer batteryLevel;
    private Integer gsmSignal;
    private LocalDateTime lastUpdate;
    private Boolean isConnected;   // novo campo boolean
    private String statusConexao;  // texto em português, pronto pro app

    public DeviceStatusDTO(Integer deviceId,
                           Integer batteryLevel,
                           Integer gsmSignal,
                           LocalDateTime lastUpdate,
                           Boolean isConnected) {
        this.deviceId = deviceId;
        this.batteryLevel = batteryLevel;
        this.gsmSignal = gsmSignal;
        this.lastUpdate = lastUpdate;
        this.isConnected = isConnected != null ? isConnected : Boolean.FALSE;
        this.statusConexao = this.isConnected ? "Conectado" : "Desconectado";
    }

    public Integer getDeviceId() { return deviceId; }
    public Integer getBatteryLevel() { return batteryLevel; }
    public Integer getGsmSignal() { return gsmSignal; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }

    /** Indica se o dispositivo está conectado (true/false). */
    public Boolean getIsConnected() { return isConnected; }

    /** Texto pronto para exibição no FlutterFlow: "Conectado" / "Desconectado". */
    public String getStatusConexao() { return statusConexao; }
}
