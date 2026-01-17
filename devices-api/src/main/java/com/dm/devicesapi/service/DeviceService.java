package com.dm.devicesapi.service;

import com.dm.devicesapi.dto.DeviceRequestDTO;
import com.dm.devicesapi.dto.DeviceResponseDTO;
import com.dm.devicesapi.mapper.DeviceMapper;
import com.dm.devicesapi.model.Device;
import com.dm.devicesapi.model.DeviceState;
import com.dm.devicesapi.repository.DeviceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DeviceService {

    private DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Page<DeviceResponseDTO> getAllDevices( String brand, String state,
                                                  int page, int size, String sortBy, String sortDir) {

        Pageable pageable = PageRequest.of(page, size,
                sortDir.equalsIgnoreCase("asc") ?
                        Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        Page<Device> devices ;

        if(brand != null && state != null){
            devices = deviceRepository.findByBrandIgnoreCaseAndState(brand, DeviceState.valueOf(state.toUpperCase()), pageable);
        } else if(brand != null){
            devices = deviceRepository.findByBrandIgnoreCase(brand, pageable);
        } else if(state != null){
            devices = deviceRepository.findByState(DeviceState.valueOf(state.toUpperCase()), pageable);
        } else {
            devices = deviceRepository.findAll(pageable);
        }
        return devices.map(DeviceMapper::toDTO);
    }

    public DeviceResponseDTO getDeviceById(UUID id) {
        Device device = deviceRepository.findById(id)
                //TODO:create custom exception later
                .orElseThrow(() -> new RuntimeException("Device with this id does not exist: " + id));
        return DeviceMapper.toDTO(device);
    }

    public DeviceResponseDTO createDevice(DeviceRequestDTO deviceRequestDTO) {
        Device device = DeviceMapper.toModel(deviceRequestDTO);
        device.setCreationTime(LocalDateTime.now());
        Device savedDevice = deviceRepository.save(device);
        return DeviceMapper.toDTO(savedDevice);
    }

    public DeviceResponseDTO updateDevice(UUID id, DeviceRequestDTO deviceRequestDTO) {

        Device device = getDeviceIfNotInUse(id);
        device.setName(deviceRequestDTO.getName());
        device.setBrand(deviceRequestDTO.getBrand());
        device.setState(DeviceState.valueOf(deviceRequestDTO.getState()));

        Device updatedDevice = deviceRepository.save(device);
        return DeviceMapper.toDTO(updatedDevice);

    }

    public DeviceResponseDTO partialUpdateDevice(UUID id, DeviceRequestDTO deviceRequestDTO) {

        Device device = getDeviceIfNotInUse(id);

        if(deviceRequestDTO.getName() != null)
            device.setName(deviceRequestDTO.getName());

        if(deviceRequestDTO.getBrand() != null)
            device.setBrand(deviceRequestDTO.getBrand());

        if(deviceRequestDTO.getState() != null)
            device.setState(DeviceState.valueOf(deviceRequestDTO.getState()));

        Device updatedDevice = deviceRepository.save(device);
        return DeviceMapper.toDTO(updatedDevice);

    }

    public void deleteDevice(UUID id) {
        Device device = getDeviceIfNotInUse(id);
        deviceRepository.delete(device);
    }

    private Device getDeviceIfExists(UUID id) {
        Device device = deviceRepository.findById(id)
                //TODO:create custom exception later
                .orElseThrow(() -> new RuntimeException("Device with this id does not exist: " + id));
        return device;
    }

    private Device getDeviceIfNotInUse(UUID id) {
        Device device = getDeviceIfExists(id);
        if(device.getState() == DeviceState.IN_USE){
            throw new RuntimeException("Device is currently in use and cannot be updated/Deleted: " + id);
        }
        return device;
    }



}
