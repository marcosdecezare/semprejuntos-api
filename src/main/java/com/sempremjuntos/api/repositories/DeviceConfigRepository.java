package com.sempremjuntos.api.repositories;

import com.sempremjuntos.api.entities.DeviceConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import com.sempremjuntos.api.entities.DeviceConfigUpdateRequest;

@Repository
public class DeviceConfigRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<DeviceConfigDTO> findConfigByDeviceId(Integer deviceId) {
        String sql = """
            SELECT 
                d.id,
                d.imei,
                d.name,
                d.photo_url,
                d.phone_number,
                d.sos_number1,
                d.sos_number2,
                d.sos_number3,
                d.fall_alert_switch,
                d.fall_sensitivity,
                d.white_list_enabled,
                d.working_mode,
                d.heart_auto_test_enabled,
                d.heart_auto_test_interval_minutes,
                d.created_at
            FROM semprejuntos.devices d
            WHERE d.id = ?
            LIMIT 1
        """;

        return jdbcTemplate.query(sql, new Object[]{deviceId}, this::mapRow)
                .stream()
                .findFirst();
    }

    private DeviceConfigDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DeviceConfigDTO(
                rs.getInt("id"),
                rs.getString("imei"),
                rs.getString("name"),
                rs.getString("photo_url"),
                rs.getString("phone_number"),
                rs.getString("sos_number1"),
                rs.getString("sos_number2"),
                rs.getString("sos_number3"),
                (Boolean) rs.getObject("fall_alert_switch"),          // pode ser null
                (Integer) rs.getObject("fall_sensitivity"),           // pode ser null
                (Boolean) rs.getObject("white_list_enabled"),         // pode ser null
                (Integer) rs.getObject("working_mode"),               // pode ser null
                (Boolean) rs.getObject("heart_auto_test_enabled"),    // pode ser null
                (Integer) rs.getObject("heart_auto_test_interval_minutes"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );



    }

    public int updateConfig(Integer deviceId, DeviceConfigUpdateRequest req) {
        String sql = """
            UPDATE semprejuntos.devices
            SET
                imei                          = COALESCE(?, imei),
                name                          = COALESCE(?, name),
                photo_url                     = COALESCE(?, photo_url),
                phone_number                  = COALESCE(?, phone_number),
                sos_number1                   = COALESCE(?, sos_number1),
                sos_number2                   = COALESCE(?, sos_number2),
                sos_number3                   = COALESCE(?, sos_number3),
                fall_alert_switch             = COALESCE(?, fall_alert_switch),
                fall_sensitivity              = COALESCE(?, fall_sensitivity),
                white_list_enabled            = COALESCE(?, white_list_enabled),
                working_mode                  = COALESCE(?, working_mode),
                heart_auto_test_enabled       = COALESCE(?, heart_auto_test_enabled),
                heart_auto_test_interval_minutes = COALESCE(?, heart_auto_test_interval_minutes)
            WHERE id = ?
        """;

        return jdbcTemplate.update(sql,
                req.getImei(),
                req.getName(),
                req.getPhotoUrl(),
                req.getPhoneNumber(),
                req.getSosNumber1(),
                req.getSosNumber2(),
                req.getSosNumber3(),
                req.getFallAlertSwitch(),
                req.getFallSensitivity(),
                req.getWhiteListEnabled(),
                req.getWorkingMode(),
                req.getHeartAutoTestEnabled(),
                req.getHeartAutoTestIntervalMinutes(),
                deviceId
        );
    }




}
