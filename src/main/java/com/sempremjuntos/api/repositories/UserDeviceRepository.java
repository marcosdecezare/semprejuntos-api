package com.sempremjuntos.api.repositories;

import com.sempremjuntos.api.entities.DeviceInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDeviceRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<DeviceInfoDTO> findDevicesByUserId(Integer userId) {
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
                d.created_at
            FROM user_devices ud
            JOIN devices d ON d.id = ud.device_id
            WHERE ud.user_id = ?
            ORDER BY d.name ASC
        """;

        return jdbcTemplate.query(sql, new Object[]{userId}, this::mapRowToDeviceInfoDTO);
    }

    private DeviceInfoDTO mapRowToDeviceInfoDTO(ResultSet rs, int rowNum) throws SQLException {
        return new DeviceInfoDTO(
                rs.getInt("id"),
                rs.getString("imei"),
                rs.getString("name"),
                rs.getString("photo_url"),
                rs.getString("phone_number"),
                rs.getString("sos_number1"),
                rs.getString("sos_number2"),
                rs.getString("sos_number3"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
