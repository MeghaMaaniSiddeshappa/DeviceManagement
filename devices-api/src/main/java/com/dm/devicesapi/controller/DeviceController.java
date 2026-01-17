package com.dm.devicesapi.controller;

import com.dm.devicesapi.dto.DevicePatchRequestDTO;
import com.dm.devicesapi.dto.DeviceRequestDTO;
import com.dm.devicesapi.dto.DeviceResponseDTO;
import com.dm.devicesapi.model.DeviceState;
import com.dm.devicesapi.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

//TODO: time format is wrong in response?
//TODO: add validation annotations to REQUESTDTOs and validate request bodies in controller methods
//TODO: Create seperate DTO for PATCH requests?


@RestController
@RequestMapping("api/v1/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping()
    public ResponseEntity<Page<DeviceResponseDTO>> getAllDevices(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) DeviceState state,
            Pageable pageable
    ) {
        Page<DeviceResponseDTO> deviceResponseDTOs = deviceService.getAllDevices( brand, state, pageable);
        return ResponseEntity.ok(deviceResponseDTOs);
    }


    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponseDTO> getDeviceById(@PathVariable UUID id) {
        DeviceResponseDTO deviceResponseDTO = deviceService.getDeviceById(id);
        return ResponseEntity.ok(deviceResponseDTO);
    }

    @PostMapping
    public ResponseEntity<DeviceResponseDTO> createDevice(@RequestBody @Valid DeviceRequestDTO deviceRequestDTO) {
        DeviceResponseDTO createdDTO = deviceService.createDevice( deviceRequestDTO);
        URI location = URI.create("/api/v1/devices/" + createdDTO.getId());
        return ResponseEntity.created(location).body(createdDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponseDTO> updateDevice(@PathVariable UUID id,
                                                         @RequestBody @Valid DeviceRequestDTO deviceRequestDTO) {
        DeviceResponseDTO updatedDTO = deviceService.updateDevice(id, deviceRequestDTO);
        return ResponseEntity.ok(updatedDTO);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<DeviceResponseDTO> patchUpdateDevice(@PathVariable UUID id,
                                                               @RequestBody DevicePatchRequestDTO devicePatchRequestDTO) {
        DeviceResponseDTO updatedDTO = deviceService.patchUpdateDevice(id, devicePatchRequestDTO);
        return ResponseEntity.ok(updatedDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }

}
