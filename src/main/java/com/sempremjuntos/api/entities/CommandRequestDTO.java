package com.sempremjuntos.api.entities;

/**
 * DTO usado para receber o comando que o servidor quer mandar para o relógio.
 */
public class CommandRequestDTO {

    private Integer deviceId;
    private String commandType;

    public CommandRequestDTO() {
    }

    public CommandRequestDTO(Integer deviceId, String commandType) {
        this.deviceId = deviceId;
        this.commandType = commandType;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }
}
