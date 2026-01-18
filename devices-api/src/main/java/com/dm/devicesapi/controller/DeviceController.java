package com.dm.devicesapi.controller;

import com.dm.devicesapi.dto.DevicePatchRequestDTO;
import com.dm.devicesapi.dto.DeviceRequestDTO;
import com.dm.devicesapi.dto.DeviceResponseDTO;
import com.dm.devicesapi.model.DeviceState;
import com.dm.devicesapi.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/devices")
@Tag(name = "Device", description = "API for managing Devices, including CRUD operations and filtering")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping()
    @Operation(
            summary = "Get all devices",
            description = "Retrieve a paginated list of devices with optional filtering by brand and state."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of devices")
    })
    public ResponseEntity<Page<DeviceResponseDTO>> getAllDevices(

            @Parameter(description = "Filter by brand", example = "OptiCam")
            @RequestParam(required = false) String brand,

            @Parameter(description = "Filter by state", example = "IN_USE")
            @RequestParam(required = false) DeviceState state,

            @Parameter(description = "Pagination information")
            Pageable pageable
    ) {
        Page<DeviceResponseDTO> deviceResponseDTOs = deviceService.getAllDevices( brand, state, pageable);
        return ResponseEntity.ok(deviceResponseDTOs);
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Get device by ID",
            description = "Retrieve a device by its unique ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the device"),
            @ApiResponse(responseCode = "400", description = "Validation error occurred"),
            @ApiResponse(responseCode = "404", description = "Device not found")
    })
    public ResponseEntity<DeviceResponseDTO> getDeviceById(
            @Parameter(description = " Device ID", example = "333e4567-e89b-12d3-a456-426614174012")
            @PathVariable UUID id
    ) {
        DeviceResponseDTO deviceResponseDTO = deviceService.getDeviceById(id);
        return ResponseEntity.ok(deviceResponseDTO);
    }

    @PostMapping
    @Operation(
            summary = "Create a new device",
            description = "Create a new device. Name, brand, and state are required."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Device created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error occurred")
    })
    public ResponseEntity<DeviceResponseDTO> createDevice(
            @RequestBody @Valid
            @Parameter(description = "Device creation payload")
            DeviceRequestDTO deviceRequestDTO
    ) {
        DeviceResponseDTO createdDTO = deviceService.createDevice( deviceRequestDTO);
        URI location = URI.create("/api/v1/devices/" + createdDTO.getId());
        return ResponseEntity.created(location).body(createdDTO);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Full Update an existing device",
            description = "Update all fields of an existing device. Name, brand, and state are required."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error occurred"),
            @ApiResponse(responseCode = "404", description = "Device not found"),
            @ApiResponse(responseCode = "409", description = "Update not allowed due to business rules")
    })
    public ResponseEntity<DeviceResponseDTO> updateDevice(
            @Parameter(description = " Device ID", example = "333e4567-e89b-12d3-a456-426614174012")
            @PathVariable UUID id,

            @RequestBody @Valid
            @Parameter(description = "Device update payload")
            DeviceRequestDTO deviceRequestDTO) {
        DeviceResponseDTO updatedDTO = deviceService.updateDevice(id, deviceRequestDTO);
        return ResponseEntity.ok(updatedDTO);
    }


    @PatchMapping("/{id}")
    @Operation(
            summary = "Partial Update an existing device",
            description = "Update only the provided fields of an existing device.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error occurred"),
            @ApiResponse(responseCode = "404", description = "Device not found"),
            @ApiResponse(responseCode = "409", description = "Update not allowed due to business rules")
    })
    public ResponseEntity<DeviceResponseDTO> patchUpdateDevice(
            @Parameter(description = " Device ID", example = "333e4567-e89b-12d3-a456-426614174012")
            @PathVariable UUID id,

            @RequestBody
            @Parameter(description = "Device update payload")
            DevicePatchRequestDTO devicePatchRequestDTO) {
        DeviceResponseDTO updatedDTO = deviceService.patchUpdateDevice(id, devicePatchRequestDTO);
        return ResponseEntity.ok(updatedDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a device")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Device deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Device not found"),
            @ApiResponse(responseCode = "409", description = "Deletion not allowed due to business rules")
    })
    public ResponseEntity<Void> deleteDevice(
            @Parameter(description = " Device ID", example = "333e4567-e89b-12d3-a456-426614174012")
            @PathVariable UUID id
    ) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }

}
