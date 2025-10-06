package com.semprejuntos.api.repositories;

import com.semprejuntos.api.entities.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceStatusRepository extends JpaRepository<DeviceStatus, Long> {


    @Query(value = "SELECT ds.id, ds.device_id, ds.battery_level, ds.gsm_signal, ds.last_update " +
            "FROM semprejuntos.device_status ds " +
            "JOIN semprejuntos.devices d ON d.id = ds.device_id " +
            "WHERE d.imei = :imei " +
            "ORDER BY ds.last_update DESC " +
            "LIMIT 1",
            nativeQuery = true)
    DeviceStatus findLatestByImei(String imei);


}
