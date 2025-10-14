package com.sempremjuntos.api.entities;

import java.time.OffsetDateTime;

public class LocationDTO {
    private Integer deviceId;
    private Double latitude;
    private Double longitude;
    private String source;        // GPS | LBS | WIFI
    private Double accuracyMeters; // opcional (LBS/WIFI)
    private OffsetDateTime timestamp;

    public LocationDTO(Integer deviceId, Double latitude, Double longitude, String source, Double accuracyMeters, OffsetDateTime timestamp) {
        this.deviceId = deviceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.source = source;
        this.accuracyMeters = accuracyMeters;
        this.timestamp = timestamp;
    }

    public Integer getDeviceId() { return deviceId; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getSource() { return source; }
    public Double getAccuracyMeters() { return accuracyMeters; }
    public OffsetDateTime getTimestamp() { return timestamp; }
}
