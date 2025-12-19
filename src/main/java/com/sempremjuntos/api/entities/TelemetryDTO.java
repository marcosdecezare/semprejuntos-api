package com.sempremjuntos.api.entities;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TelemetryDTO {

    private Integer id;
    private Integer deviceId;
    private String packetType;
    private String rawPayload;
    private String createdAt;  // ✅ padronizado como String
    private String clientIp;

    private static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo");
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ✅ Construtor principal (OffsetDateTime)
    public TelemetryDTO(Integer id,
                        Integer deviceId,
                        String packetType,
                        String rawPayload,
                        OffsetDateTime createdAt,
                        String clientIp) {
        this.id = id;
        this.deviceId = deviceId;
        this.packetType = packetType;
        this.rawPayload = rawPayload;
        this.createdAt = formatTimestamp(createdAt);
        this.clientIp = clientIp;
    }

    // ✅ Alternativo (String já formatada)
    public TelemetryDTO(Integer id,
                        Integer deviceId,
                        String packetType,
                        String rawPayload,
                        String createdAtFormatted,
                        String clientIp) {
        this.id = id;
        this.deviceId = deviceId;
        this.packetType = packetType;
        this.rawPayload = rawPayload;
        this.createdAt = createdAtFormatted;
        this.clientIp = clientIp;
    }

    private String formatTimestamp(OffsetDateTime ts) {
        if (ts == null) return null;
        return ts.atZoneSameInstant(ZONE).format(FORMATTER);
    }

    public Integer getId() { return id; }
    public Integer getDeviceId() { return deviceId; }
    public String getPacketType() { return packetType; }
    public String getRawPayload() { return rawPayload; }
    public String getCreatedAt() { return createdAt; }
    public String getClientIp() { return clientIp; }
}
