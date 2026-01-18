package com.dm.devicesapi.dto;

import com.dm.devicesapi.model.DeviceState;

public class DevicePatchRequestDTO {

    private String name;
    private String brand;
    private DeviceState state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public DeviceState getState() {
        return state;
    }

    public void setState(DeviceState state) {
        this.state = state;
    }
}