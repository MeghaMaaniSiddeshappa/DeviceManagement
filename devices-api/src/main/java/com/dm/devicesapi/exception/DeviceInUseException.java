package com.dm.devicesapi.exception;

public class DeviceInUseException extends RuntimeException{
    public DeviceInUseException(String message) {
        super(message);
    }
}