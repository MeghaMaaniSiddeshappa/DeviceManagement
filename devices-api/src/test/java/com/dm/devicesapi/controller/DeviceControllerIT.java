package com.dm.devicesapi.controller;

import com.dm.devicesapi.BaseIntegrationTest;
import com.dm.devicesapi.dto.DeviceRequestDTO;
import com.dm.devicesapi.dto.DeviceResponseDTO;
import com.dm.devicesapi.model.DeviceState;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class DeviceControllerIT extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndFetchDevice() throws Exception {
        DeviceRequestDTO deviceRequestDTO = new DeviceRequestDTO();
        deviceRequestDTO.setName("Test Device");
        deviceRequestDTO.setBrand("Test Brand");
        deviceRequestDTO.setState(DeviceState.AVAILABLE);

        // Create
        String createResponse = mockMvc.perform(post("/api/v1/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deviceRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DeviceResponseDTO created = objectMapper.readValue(createResponse, DeviceResponseDTO.class);

        // Fetch
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/v1/devices/" + created.getId()))
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Test Device"))
        .andExpect(jsonPath("$.brand").value("Test Brand"));

    }

    @Test
    void shouldNotDeleteDeviceInUse() throws Exception {

        DeviceRequestDTO request = new DeviceRequestDTO();
        request.setName("Device A");
        request.setBrand("Brand A");
        request.setState(DeviceState.IN_USE);

        String response = mockMvc.perform(post("/api/v1/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        DeviceResponseDTO created =
                objectMapper.readValue(response, DeviceResponseDTO.class);

        mockMvc.perform(delete("/api/v1/devices/{id}", created.getId()))
                .andExpect(status().isConflict());
    }

}
