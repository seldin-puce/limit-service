package com.limit.service.limit.model;


import javax.persistence.*;
import java.util.Calendar;

@Entity
@IdClass(BlockedDeviceKey.class)
@Table(name = "BlockedDevice")
public class BlockedDevice {

    @Id
    private String messageId;
    @Id
    private String deviceId;
    @Id
    private String limitId;
    private Calendar blockedAt;
    private Calendar blockExpiration;
    private boolean active;

    public BlockedDevice(String deviceId, String ticketMessageId, String limitId, Calendar blockedAt, Calendar blockExpiration) {
        this.deviceId = deviceId;
        this.messageId = ticketMessageId;
        this.limitId = limitId;
        this.blockedAt = blockedAt;
        this.blockExpiration = blockExpiration;
        this.active = true;
    }

    public BlockedDevice() {
    }

    @Override
    public int hashCode() {
        String hash = this.messageId + this.deviceId + this.limitId;
        return hash.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        BlockedDevice blockedDevice = (BlockedDevice) obj;
        String identifier = blockedDevice.getMessageId() + blockedDevice.getDeviceId() + blockedDevice.getLimitId();
        String identifier1 = this.messageId + this.deviceId + this.limitId;
        return identifier.equals(identifier1);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getLimitId() {
        return limitId;
    }
}
