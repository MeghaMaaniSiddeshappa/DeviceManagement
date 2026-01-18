package com.dm.devicesapi.service;

import com.dm.devicesapi.dto.DevicePatchRequestDTO;
import com.dm.devicesapi.dto.DeviceRequestDTO;
import com.dm.devicesapi.dto.DeviceResponseDTO;
import com.dm.devicesapi.exception.DeviceInUseException;
import com.dm.devicesapi.exception.DeviceNotFoundException;
import com.dm.devicesapi.model.Device;
import com.dm.devicesapi.model.DeviceState;
import com.dm.devicesapi.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;

    private Device device;
    private UUID deviceId;

    @BeforeEach
    void setUp() {
        deviceId = UUID.randomUUID();
        device = new Device();
        device.setId(deviceId);
        device.setBrand("Apple");
        device.setName("iphone 13");
        device.setState(DeviceState.AVAILABLE);
        device.setCreationTime(LocalDateTime.now());
    }

    @Test
    void should_GetDeviceById_Successfully() {
        when (deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));

        DeviceResponseDTO resp = deviceService.getDeviceById(deviceId);
        assertNotNull(resp);
        assertEquals("iphone 13", resp.getName());
        verify(deviceRepository).findById(deviceId);
    }

    @Test
    void shouldThrow_whenGetDeviceById_NotExists() {
        when (deviceRepository.findById(deviceId)).thenReturn(Optional.empty());

       assertThrows(DeviceNotFoundException.class, () -> deviceService.getDeviceById(deviceId));
    }

    @Test
    void should_CreateDevice_Successfully() {
        DeviceRequestDTO deviceRequestDTO = new DeviceRequestDTO();
        deviceRequestDTO.setBrand("Apple");
        deviceRequestDTO.setName("iphone 13");
        deviceRequestDTO.setState(DeviceState.AVAILABLE);

        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        DeviceResponseDTO resp = deviceService.createDevice(deviceRequestDTO);

        assertNotNull(resp);
        verify(deviceRepository).save(any(Device.class));
    }

    @Test
    void should_UpdateDevice_Successfully() {
        DeviceRequestDTO deviceRequestDTO = new DeviceRequestDTO();
        deviceRequestDTO.setBrand("Apple");
        deviceRequestDTO.setName("iphone 13 Pro");
        deviceRequestDTO.setState(DeviceState.AVAILABLE);

        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));
        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        DeviceResponseDTO resp = deviceService.updateDevice(deviceId, deviceRequestDTO);

        assertNotNull(resp);
        assertEquals("iphone 13 Pro", resp.getName());
        verify(deviceRepository).findById(deviceId);
        verify(deviceRepository).save(any(Device.class));
    }

    @Test
    void shouldThrow_WhenUpdatingNameOrBrand_WhileInUse() {
        device.setState(DeviceState.IN_USE);

        DeviceRequestDTO deviceRequestDTO = new DeviceRequestDTO();
        deviceRequestDTO.setBrand("New Brand");
        deviceRequestDTO.setName("New Name");
        deviceRequestDTO.setState(DeviceState.IN_USE);

        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));
        assertThrows(DeviceInUseException.class, () -> deviceService.updateDevice(deviceId, deviceRequestDTO));

        verify(deviceRepository, never()).save(any());
    }

    @Test
    void should_PatchUpdateDevice_Successfully() {
        DevicePatchRequestDTO requestDTO = new DevicePatchRequestDTO();
        requestDTO.setState(DeviceState.IN_USE);

        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));
        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        DeviceResponseDTO resp = deviceService.patchUpdateDevice(deviceId, requestDTO);

        assertNotNull(resp);
        verify(deviceRepository).save(any(Device.class));
    }

    @Test
    void shouldThrow_WhenPatchingNameOrBrand_WhileInUse() {
        device.setState(DeviceState.IN_USE);

        DevicePatchRequestDTO requestDTO = new DevicePatchRequestDTO();
        requestDTO.setName("New Name");

        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));
        assertThrows(DeviceInUseException.class, () -> deviceService.patchUpdateDevice(deviceId, requestDTO));

        verify(deviceRepository, never()).save(any());
    }

    @Test
    void should_DeleteDevice_Successfully() {
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));

        deviceService.deleteDevice(deviceId);

        verify(deviceRepository).delete(device);
    }

    @Test
    void shouldThrow_WhenDeletingDevice_InUse() {
        device.setState(DeviceState.IN_USE);

        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));

        assertThrows(DeviceInUseException.class, () -> deviceService.deleteDevice(deviceId));

        verify(deviceRepository, never()).delete(any());
    }
}
