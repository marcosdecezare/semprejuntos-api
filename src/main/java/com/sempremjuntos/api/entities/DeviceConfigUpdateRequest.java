package com.sempremjuntos.api.entities;

public class DeviceConfigUpdateRequest {

    private String imei;
    private String name;
    private String photoUrl;
    private String phoneNumber;
    private String sosNumber1;
    private String sosNumber2;
    private String sosNumber3;
    private Boolean fallAlertSwitch;
    private Integer fallSensitivity;
    private Boolean whiteListEnabled;
    private Integer workingMode;
    private Boolean heartAutoTestEnabled;
    private Integer heartAutoTestIntervalMinutes;

    public DeviceConfigUpdateRequest() {
        // necessário para desserialização JSON
    }

    public String getImei() { return imei; }
    public void setImei(String imei) { this.imei = imei; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getSosNumber1() { return sosNumber1; }
    public void setSosNumber1(String sosNumber1) { this.sosNumber1 = sosNumber1; }

    public String getSosNumber2() { return sosNumber2; }
    public void setSosNumber2(String sosNumber2) { this.sosNumber2 = sosNumber2; }

    public String getSosNumber3() { return sosNumber3; }
    public void setSosNumber3(String sosNumber3) { this.sosNumber3 = sosNumber3; }

    public Boolean getFallAlertSwitch() { return fallAlertSwitch; }
    public void setFallAlertSwitch(Boolean fallAlertSwitch) { this.fallAlertSwitch = fallAlertSwitch; }

    public Integer getFallSensitivity() { return fallSensitivity; }
    public void setFallSensitivity(Integer fallSensitivity) { this.fallSensitivity = fallSensitivity; }

    public Boolean getWhiteListEnabled() { return whiteListEnabled; }
    public void setWhiteListEnabled(Boolean whiteListEnabled) { this.whiteListEnabled = whiteListEnabled; }

    public Integer getWorkingMode() { return workingMode; }
    public void setWorkingMode(Integer workingMode) { this.workingMode = workingMode; }

    public Boolean getHeartAutoTestEnabled() { return heartAutoTestEnabled; }
    public void setHeartAutoTestEnabled(Boolean heartAutoTestEnabled) { this.heartAutoTestEnabled = heartAutoTestEnabled; }

    public Integer getHeartAutoTestIntervalMinutes() { return heartAutoTestIntervalMinutes; }
    public void setHeartAutoTestIntervalMinutes(Integer heartAutoTestIntervalMinutes) { this.heartAutoTestIntervalMinutes = heartAutoTestIntervalMinutes; }
}
