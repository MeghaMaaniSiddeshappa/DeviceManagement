package com.dm.devicesapi.service;

import com.dm.devicesapi.dto.DevicePatchRequestDTO;
import com.dm.devicesapi.dto.DeviceRequestDTO;
import com.dm.devicesapi.dto.DeviceResponseDTO;
import com.dm.devicesapi.exception.DeviceInUseException;
import com.dm.devicesapi.exception.DeviceNotFoundException;
import com.dm.devicesapi.mapper.DeviceMapper;
import com.dm.devicesapi.model.Device;
import com.dm.devicesapi.model.DeviceState;
import com.dm.devicesapi.repository.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
public class DeviceService {

    private static final Logger log = LoggerFactory.getLogger(DeviceService.class);

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Transactional(readOnly = true)
    public Page<DeviceResponseDTO> getAllDevices( String brand, DeviceState state,
                                                Pageable pageable) {
        log.info("Retrieving devices with filters - Brand: {}, State: {}", brand, state);
        Page<Device> devices ;
        if(brand != null && state != null){
            devices = deviceRepository.findByBrandIgnoreCaseAndState(brand, state, pageable);
        } else if(brand != null){
            devices = deviceRepository.findByBrandIgnoreCase(brand, pageable);
        } else if(state != null){
            devices = deviceRepository.findByState(state, pageable);
        } else {
            devices = deviceRepository.findAll(pageable);
        }
        log.info("Retrieved {} devices", devices.getTotalElements());
        return devices.map(DeviceMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public DeviceResponseDTO getDeviceById(UUID id) {
        log.info("Retrieving device with ID: {}", id);
        Device device = getDeviceIfExists(id);
        return DeviceMapper.toDTO(device);
    }

    public DeviceResponseDTO createDevice(DeviceRequestDTO deviceRequestDTO) {
        log.info("Creating new device");
        Device device = DeviceMapper.toModel(deviceRequestDTO);
        Device savedDevice = deviceRepository.save(device);
        log.info("Created device with ID: {}", savedDevice.getId());
        return DeviceMapper.toDTO(savedDevice);
    }

    public DeviceResponseDTO updateDevice(UUID id, DeviceRequestDTO deviceRequestDTO) {

        log.info("Updating device with ID: {}", id);
        Device device = getDeviceIfExists(id);

        if(device.getState() == DeviceState.IN_USE &&
                (!Objects.equals(device.getBrand(), deviceRequestDTO.getBrand()) ||
                        !Objects.equals(device.getName(), deviceRequestDTO.getName()))) {
            log.warn("Attempt to update Name/Brand while device is in use: {}", id);
            throw new DeviceInUseException("Name/Brand cannot be updated while device is in use: " + id);
        }

        device.setName(deviceRequestDTO.getName());
        device.setBrand(deviceRequestDTO.getBrand());
        device.setState(deviceRequestDTO.getState());

        Device updatedDevice = deviceRepository.save(device);
        log.info("Updated device successfully");
        return DeviceMapper.toDTO(updatedDevice);

    }

    public DeviceResponseDTO patchUpdateDevice(UUID id, DevicePatchRequestDTO devicePatchRequestDTO) {

        log.info("Patch Updating device with ID: {}", id);
        Device device = getDeviceIfExists(id);

        if(device.getState() == DeviceState.IN_USE &&
                (devicePatchRequestDTO.getName() != null || devicePatchRequestDTO.getBrand() != null)) {
            log.warn("Attempt to patch update Name/Brand while device is in use: {}", id);
            throw new DeviceInUseException("Name/Brand cannot be updated while device is in use: " + id);
        }

        if(devicePatchRequestDTO.getName() != null)
            device.setName(devicePatchRequestDTO.getName());

        if(devicePatchRequestDTO.getBrand() != null)
            device.setBrand(devicePatchRequestDTO.getBrand());

        if(devicePatchRequestDTO.getState() != null)
            device.setState(devicePatchRequestDTO.getState());

        Device updatedDevice = deviceRepository.save(device);
        log.info("Patch updated device successfully");
        return DeviceMapper.toDTO(updatedDevice);

    }

    public void deleteDevice(UUID id) {
        log.info("Deleting device with ID: {}", id);
        Device device = getDeviceIfNotInUse(id);
        deviceRepository.delete(device);
        log.info("Deleted device successfully");
    }

    private Device getDeviceIfExists(UUID id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Device with ID: {} not found", id);
                   return new DeviceNotFoundException("Device with this id does not exist: " + id);
                });
    }


    private Device getDeviceIfNotInUse(UUID id) {
        Device device = getDeviceIfExists(id);
        if(device.getState() == DeviceState.IN_USE){
            log.warn("Attempt to delete device while in use: {}", id);
            throw new DeviceInUseException("Device is currently in use and cannot be Deleted: " + id);
        }
        return device;
    }

}
