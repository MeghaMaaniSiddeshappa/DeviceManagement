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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Page<DeviceResponseDTO> getAllDevices( String brand, DeviceState state,
                                                Pageable pageable) {
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
        return devices.map(DeviceMapper::toDTO);
    }

    public DeviceResponseDTO getDeviceById(UUID id) {
        Device device = getDeviceIfExists(id);
        return DeviceMapper.toDTO(device);
    }

    public DeviceResponseDTO createDevice(DeviceRequestDTO deviceRequestDTO) {
        Device device = DeviceMapper.toModel(deviceRequestDTO);
        Device savedDevice = deviceRepository.save(device);
        return DeviceMapper.toDTO(savedDevice);
    }

    public DeviceResponseDTO updateDevice(UUID id, DeviceRequestDTO deviceRequestDTO) {

        Device device = getDeviceIfExists(id);

        if(device.getState() == DeviceState.IN_USE &&
                (!Objects.equals(device.getBrand(), deviceRequestDTO.getBrand()) ||
                        !Objects.equals(device.getName(), deviceRequestDTO.getName()))) {
            throw new DeviceInUseException("Name/Brand cannot be updated while device is in use: " + id);
        }

        device.setName(deviceRequestDTO.getName());
        device.setBrand(deviceRequestDTO.getBrand());
        device.setState(deviceRequestDTO.getState());

        Device updatedDevice = deviceRepository.save(device);
        return DeviceMapper.toDTO(updatedDevice);

    }

    public DeviceResponseDTO patchUpdateDevice(UUID id, DevicePatchRequestDTO devicePatchRequestDTO) {

        Device device = getDeviceIfExists(id);

        if(device.getState() == DeviceState.IN_USE &&
                (devicePatchRequestDTO.getName() != null || devicePatchRequestDTO.getBrand() != null)) {
            throw new DeviceInUseException("Name/Brand cannot be updated while device is in use: " + id);
        }

        if(devicePatchRequestDTO.getName() != null)
            device.setName(devicePatchRequestDTO.getName());

        if(devicePatchRequestDTO.getBrand() != null)
            device.setBrand(devicePatchRequestDTO.getBrand());

        if(devicePatchRequestDTO.getState() != null)
            device.setState(DeviceState.valueOf(devicePatchRequestDTO.getState()));

        Device updatedDevice = deviceRepository.save(device);
        return DeviceMapper.toDTO(updatedDevice);

    }

    public void deleteDevice(UUID id) {
        Device device = getDeviceIfNotInUse(id);
        deviceRepository.delete(device);
    }

    private Device getDeviceIfExists(UUID id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device with this id does not exist: " + id));
    }


    private Device getDeviceIfNotInUse(UUID id) {
        Device device = getDeviceIfExists(id);
        if(device.getState() == DeviceState.IN_USE){
            throw new DeviceInUseException("Device is currently in use and cannot be Deleted: " + id);
        }
        return device;
    }

}
