package com.dm.devicesapi.mapper;

import com.dm.devicesapi.dto.DeviceRequestDTO;
import com.dm.devicesapi.dto.DeviceResponseDTO;
import com.dm.devicesapi.model.Device;
import com.dm.devicesapi.model.DeviceState;

public class DeviceMapper {

    public static DeviceResponseDTO toDTO(Device device) {
        DeviceResponseDTO deviceResponseDTO = new DeviceResponseDTO();
        deviceResponseDTO.setId(device.getId().toString());
        deviceResponseDTO.setName(device.getName());
        deviceResponseDTO.setBrand(device.getBrand());
        deviceResponseDTO.setState(device.getState().name());
        deviceResponseDTO.setCreationTime(device.getCreationTime().toString());
        return deviceResponseDTO;
    }

    public static Device toModel(DeviceRequestDTO deviceRequestDTO) {
        Device device = new Device();
        device.setName(deviceRequestDTO.getName());
        device.setBrand(deviceRequestDTO.getBrand());
        device.setState(Enum.valueOf(DeviceState.class, deviceRequestDTO.getState()));
        return device;
    }
}
