package com.dm.devicesapi.repository;

import com.dm.devicesapi.model.Device;
import com.dm.devicesapi.model.DeviceState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeviceRepository  extends JpaRepository<Device, UUID> {

    Page<Device> findByBrandIgnoreCaseAndState(String brand, DeviceState deviceState, Pageable pageable);

    Page<Device> findByBrandIgnoreCase(String brand, Pageable pageable);

    Page<Device> findByState(DeviceState deviceState, Pageable pageable);
}
