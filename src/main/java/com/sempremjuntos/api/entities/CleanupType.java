package com.sempremjuntos.api.entities;

public enum CleanupType {
    HEART,      // limpa heart_rate
    SYS_DIA,    // limpa systolic_bp e diastolic_bp (juntos)
    SPO,        // limpa spo2
    TEMP,       // limpa body_temperature
    STEP,       // limpa step_count
    ALL;        // limpa todas as linhas do device_id

    public static CleanupType from(String s) {
        if (s == null) return null;
        try {
            return CleanupType.valueOf(s.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
