package com.limit.service.limit.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "LimitInfo")
public class Limit {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private Calendar createdAt;
    private Calendar lastsUntil;
    private Calendar endedAt;
    private int limitValue;
    private int hotPercentage;
    private int restrictionExpiration;
    private boolean active;
    @OneToMany(targetEntity = TicketMessage.class,
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "limitInfoId", referencedColumnName = "id")
    private List<TicketMessage> ticketMessages;
    @OneToMany(targetEntity = BlockedDevice.class,
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "limitId", referencedColumnName = "id")
    private Set<BlockedDevice> blockedDevices;

    protected Limit() {
    }

    public Limit(int duration, Integer limit, Integer hotPercentage, Integer restrictionExpires) {
        this.createdAt = Calendar.getInstance();
        Calendar ends = Calendar.getInstance();
        ends.add(Calendar.SECOND, duration);
        this.lastsUntil = ends;
        this.endedAt = null;
        this.limitValue = limit;
        this.hotPercentage = hotPercentage;
        this.restrictionExpiration = restrictionExpires;
        this.active = true;
        this.ticketMessages = null;
    }

    public String getId() {
        return id;
    }

    public void setEndedAt(Calendar duration) {
        this.endedAt = duration;
    }

    public Integer getLimitValue() {
        return limitValue;
    }

    public Integer getHotPercentage() {
        return hotPercentage;
    }

    public Integer getRestrictionExpiration() {
        return restrictionExpiration;
    }

}