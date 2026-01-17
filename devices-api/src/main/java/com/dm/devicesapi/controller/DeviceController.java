package com.dm.devicesapi.controller;

import com.dm.devicesapi.dto.DeviceRequestDTO;
import com.dm.devicesapi.dto.DeviceResponseDTO;
import com.dm.devicesapi.service.DeviceService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(required = false) String state,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir
    ) {
        Page<DeviceResponseDTO> deviceResponseDTOs = deviceService.getAllDevices( brand, state, page, size, sortBy, sortDir);
        return ResponseEntity.ok().body(deviceResponseDTOs);
    }


    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponseDTO> getDeviceById(@PathVariable UUID id) {
        DeviceResponseDTO deviceResponseDTO = deviceService.getDeviceById(id);
        return ResponseEntity.ok().body(deviceResponseDTO);
    }

    @PostMapping
    public ResponseEntity<DeviceResponseDTO> createDevice(//TODO: is DeviceRequestDTO required or DeviceResponseDTO can be used here?
                                                         @RequestBody DeviceRequestDTO deviceRequestDTO) {
        DeviceResponseDTO deviceResponseDTO = deviceService.createDevice( deviceRequestDTO);
        return ResponseEntity.status(201).body(deviceResponseDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponseDTO> updateDevice(@PathVariable UUID id,
                                                         //TODO: is DeviceRequestDTO required or DeviceResponseDTO can be used here?
                                                         @RequestBody DeviceRequestDTO deviceRequestDTO) {
        DeviceResponseDTO deviceResponseDTO = deviceService.updateDevice(id, deviceRequestDTO);
        return ResponseEntity.ok().body(deviceResponseDTO);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<DeviceResponseDTO> partialUpdateDevice(@PathVariable UUID id,
                                                                //TODO: is DeviceRequestDTO required or DeviceResponseDTO can be used here?
                                                                @RequestBody DeviceRequestDTO deviceRequestDTO) {
        DeviceResponseDTO deviceResponseDTO = deviceService.partialUpdateDevice(id, deviceRequestDTO);
        return ResponseEntity.ok().body(deviceResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }




}
