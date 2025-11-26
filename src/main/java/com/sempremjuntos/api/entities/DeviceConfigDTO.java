package com.sempremjuntos.api.entities;

import java.time.LocalDateTime;

public class DeviceConfigDTO {
    private Integer id;
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
    private LocalDateTime createdAt;

    public DeviceConfigDTO(
            Integer id,
            String imei,
            String name,
            String photoUrl,
            String phoneNumber,
            String sosNumber1,
            String sosNumber2,
            String sosNumber3,
            Boolean fallAlertSwitch,
            Integer fallSensitivity,
            Boolean whiteListEnabled,
            Integer workingMode,
            Boolean heartAutoTestEnabled,
            Integer heartAutoTestIntervalMinutes,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.imei = imei;
        this.name = name;
        this.photoUrl = photoUrl;
        this.phoneNumber = phoneNumber;
        this.sosNumber1 = sosNumber1;
        this.sosNumber2 = sosNumber2;
        this.sosNumber3 = sosNumber3;
        this.fallAlertSwitch = fallAlertSwitch;
        this.fallSensitivity = fallSensitivity;
        this.whiteListEnabled = whiteListEnabled;
        this.workingMode = workingMode;
        this.heartAutoTestEnabled = heartAutoTestEnabled;
        this.heartAutoTestIntervalMinutes = heartAutoTestIntervalMinutes;
        this.createdAt = createdAt;
    }

    public Integer getId() { return id; }
    public String getImei() { return imei; }
    public String getName() { return name; }
    public String getPhotoUrl() { return photoUrl; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getSosNumber1() { return sosNumber1; }
    public String getSosNumber2() { return sosNumber2; }
    public String getSosNumber3() { return sosNumber3; }
    public Boolean getFallAlertSwitch() { return fallAlertSwitch; }
    public Integer getFallSensitivity() { return fallSensitivity; }
    public Boolean getWhiteListEnabled() { return whiteListEnabled; }
    public Integer getWorkingMode() { return workingMode; }
    public Boolean getHeartAutoTestEnabled() { return heartAutoTestEnabled; }
    public Integer getHeartAutoTestIntervalMinutes() { return heartAutoTestIntervalMinutes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
