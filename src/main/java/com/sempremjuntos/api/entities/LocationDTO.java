package com.sempremjuntos.api.entities;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * DTO para envio da localização ao front-end (FlutterFlow).
 * Agora com:
 *  - timestamp já formatado como DD-MM-YYYY HH:MM
 *  - quality em português (ALTA / MÉDIA / BAIXA / DESCONHECIDA)
 *  - description amigável totalmente em português
 *  - latLng pronto para Google Maps no FlutterFlow
 */
public class LocationDTO {

    private Integer deviceId;
    private Double latitude;
    private Double longitude;
    private String source;
    private Double accuracyMeters;
    private String timestamp;   // <-- agora é String formatada
    private String quality;     // ALTA / MEDIA / BAIXA / DESCONHECIDA
    private String description; // pronto para exibição no app
    private String latLng;

    private static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo");
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ✅ Construtor original (OffsetDateTime)
    public LocationDTO(Integer deviceId,
                       Double latitude,
                       Double longitude,
                       String source,
                       Double accuracyMeters,
                       OffsetDateTime timestamp) {

        this.deviceId = deviceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.source = source;
        this.accuracyMeters = accuracyMeters;
        this.timestamp = formatTimestamp(timestamp);
        this.quality = classifyQuality(accuracyMeters);
        this.description = buildDescription(source, this.quality);
        this.latLng = buildLatLngString(latitude, longitude);
    }

    // ✅ Novo construtor (String já formatada)
    public LocationDTO(Integer deviceId,
                       Double latitude,
                       Double longitude,
                       String source,
                       Double accuracyMeters,
                       String timestampFormatted) {

        this.deviceId = deviceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.source = source;
        this.accuracyMeters = accuracyMeters;
        this.timestamp = timestampFormatted; // já vem pronto
        this.quality = classifyQuality(accuracyMeters);
        this.description = buildDescription(source, this.quality);
        this.latLng = buildLatLngString(latitude, longitude);
    }

    /**
     * Converte o OffsetDateTime para string DD-MM-YYYY HH:mm no fuso de São Paulo.
     */
    private String formatTimestamp(OffsetDateTime ts) {
        if (ts == null) return null;
        return ts.atZoneSameInstant(ZONE).format(FORMATTER);
    }

    /**
     * Converte o valor da precisão em uma classificação em português.
     */
    private String classifyQuality(Double acc) {
        if (acc == null) return "DESCONHECIDA";
        if (acc <= 100) return "ALTA";
        if (acc <= 1000) return "MÉDIA";
        return "BAIXA";
    }

    /**
     * Monta a descrição amigável em português para exibir no app.
     */
    private String buildDescription(String source, String quality) {
        String origem = switch (source != null ? source.toUpperCase() : "") {
            case "GPS" -> "GPS";
            case "LBS" -> "Rede móvel (LBS)";
            case "WIFI" -> "Wi-Fi";
            default -> "Origem desconhecida";
        };

        String precisao = switch (quality) {
            case "ALTA" -> "com alta precisão";
            case "MÉDIA" -> "com precisão média";
            case "BAIXA" -> "com baixa precisão";
            default -> "com precisão desconhecida";
        };

        return origem + " " + precisao;
    }

    /**
     * Cria campo "latLng" no formato exato exigido pelo FlutterFlow / Google Maps.
     */
    private String buildLatLngString(Double lat, Double lng) {
        if (lat != null && lng != null && lat != 0.0 && lng != 0.0) {
            return String.format("%.7f, %.7f", lat, lng);
        }
        return null;
    }

    public Integer getDeviceId() { return deviceId; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getSource() { return source; }
    public Double getAccuracyMeters() { return accuracyMeters; }
    public String getTimestamp() { return timestamp; }
    public String getQuality() { return quality; }
    public String getDescription() { return description; }
    public String getLatLng() { return latLng; }
}
