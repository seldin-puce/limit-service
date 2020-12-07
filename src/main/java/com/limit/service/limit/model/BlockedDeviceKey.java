package com.limit.service.limit.model;

import java.io.Serializable;

public class BlockedDeviceKey implements Serializable {
    private String messageId;
    private String deviceId;
    private String limitId;

    public BlockedDeviceKey() {
    }

    public BlockedDeviceKey(String messageId, String deviceId, String limitId) {
        this.messageId = messageId;
        this.deviceId = deviceId;
        this.limitId = limitId;
    }
}
