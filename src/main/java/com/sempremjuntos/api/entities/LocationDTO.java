package com.sempremjuntos.api.entities;

import java.time.OffsetDateTime;

/**
 * DTO para envio da localização ao front-end (FlutterFlow).
 * Inclui classificação de precisão, descrição de origem amigável
 * e campo adicional "latLng" no formato "-23.5558, -46.6396".
 */
public class LocationDTO {
    private Integer deviceId;
    private Double latitude;
    private Double longitude;
    private String source;
    private Double accuracyMeters;
    private OffsetDateTime timestamp;
    private String quality;     // HIGH / MEDIUM / LOW
    private String description; // mensagem amigável para o app
    private String latLng;      // novo campo no formato "lat, lng"

    public LocationDTO(Integer deviceId, Double latitude, Double longitude,
                       String source, Double accuracyMeters, OffsetDateTime timestamp) {
        this.deviceId = deviceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.source = source;
        this.accuracyMeters = accuracyMeters;
        this.timestamp = timestamp;
        this.quality = classifyQuality(accuracyMeters);
        this.description = describe(source, this.quality);
        this.latLng = buildLatLngString(latitude, longitude);
    }

    /**
     * Cria o campo "latLng" no formato "lat, lng" (ou null se inválido).
     */
    private String buildLatLngString(Double lat, Double lng) {
        if (lat != null && lng != null && lat != 0.0 && lng != 0.0) {
            // Usa ponto decimal e separador vírgula com espaço — compatível com FlutterFlow e Google Maps
            return String.format("%.7f, %.7f", lat, lng);
        }
        return null;
    }

    private String classifyQuality(Double acc) {
        if (acc == null) return "UNKNOWN";
        if (acc <= 100) return "HIGH";
        if (acc <= 1000) return "MEDIUM";
        return "LOW";
    }

    private String describe(String source, String quality) {
        String prefix = switch (source != null ? source.toUpperCase() : "") {
            case "GPS" -> "Localização precisa via GPS";
            case "LBS" -> "Localização aproximada via rede móvel";
            case "WIFI" -> "Localização aproximada via Wi-Fi";
            default -> "Fonte de localização desconhecida";
        };
        return switch (quality) {
            case "HIGH" -> prefix + " (precisão alta)";
            case "MEDIUM" -> prefix + " (precisão média)";
            case "LOW" -> prefix + " (precisão baixa)";
            default -> prefix;
        };
    }

    public Integer getDeviceId() { return deviceId; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getSource() { return source; }
    public Double getAccuracyMeters() { return accuracyMeters; }
    public OffsetDateTime getTimestamp() { return timestamp; }
    public String getQuality() { return quality; }
    public String getDescription() { return description; }
    public String getLatLng() { return latLng; }
}
