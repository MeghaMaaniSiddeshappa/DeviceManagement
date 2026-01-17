package com.dm.devicesapi.dto;

public class DeviceRequestDTO {

    private String id;
    private String name;
    private String brand;
    private String state;

    public DeviceRequestDTO() {
    }

    public DeviceRequestDTO(String id, String name, String brand, String state) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
