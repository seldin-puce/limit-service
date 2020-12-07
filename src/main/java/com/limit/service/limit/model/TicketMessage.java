package com.limit.service.limit.model;

import javax.persistence.*;

@Entity
@IdClass(TicketMessageKey.class)
public class TicketMessage {

    @Id
    private String id;
    @Id
    private String deviceId;
    @Column(nullable = false)
    private double stake;
    private String limitInfoId;
    @ManyToOne(targetEntity = Limit.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Transient
    private Limit limit;
    @OneToOne(targetEntity = BlockedDevice.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "messageId", referencedColumnName = "id")
    @JoinColumn(name = "deviceId", referencedColumnName = "deviceId")
    @Transient
    private BlockedDevice blockedDevice;

    protected TicketMessage() {
    }

    @Override
    public boolean equals(Object obj) {
        TicketMessage ticketMessage = (TicketMessage) obj;
        return ticketMessage.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        String hash = this.id + this.deviceId;
        return hash.hashCode();
    }

    public Limit getLimit() {
        return this.limit;
    }

    public void setLimit(Limit limitPercentage) {
        this.limit = limitPercentage;
    }

    public String getId() {
        return id;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public double getStake() {
        return this.stake;
    }

    public void setLimitInfoId(String limitInfoId) {
        this.limitInfoId = limitInfoId;
    }
}
