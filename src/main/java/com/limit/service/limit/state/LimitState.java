package com.limit.service.limit.state;

import com.limit.service.limit.repository.IBlockedDeviceRepository;
import com.limit.service.limit.repository.ILimitRepository;
import com.limit.service.limit.model.BlockedDevice;
import com.limit.service.limit.model.Limit;
import com.limit.service.limit.model.TicketMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LimitState implements InitializingBean {

    @Autowired
    IBlockedDeviceRepository blockedDeviceService;

    @Autowired
    ILimitRepository limitRepository;

    private Limit limit;
    private Set<TicketMessage> trackedTicketMessages;
    private Set<BlockedDevice> blockedDeviceList;

    public LimitState() {
        this.limit = null;
        this.trackedTicketMessages = new HashSet<>();
        this.blockedDeviceList = new HashSet<>();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.blockedDeviceList = new HashSet<>(blockedDeviceService.findByActive(true));
        this.limit = this.limitRepository.findByActive(true);
    }

    public Limit getLimit() {
        return this.limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
        this.trackedTicketMessages = new HashSet<>();
    }


    public Set<TicketMessage> getTrackedTicketMessages() {
        return this.trackedTicketMessages;
    }

    public void addTicketMessage(TicketMessage ticketMessage) {
        this.trackedTicketMessages.add(ticketMessage);
    }

    public void blockDevice(BlockedDevice blockedDevice) {
        this.blockedDeviceList.add(blockedDevice);
    }

    public boolean isDeviceBlocked(String deviceId) {
        for (BlockedDevice blockedDevice : this.blockedDeviceList) {
            if (blockedDevice.getDeviceId().equals(deviceId)) {
                return true;
            }
        }
        return false;
    }

    public Runnable unblockDevice(String messageId, String deviceId, String limitId) {
        return () -> {
            this.blockedDeviceService.setLimitActive(messageId, deviceId, limitId, false);
            for (BlockedDevice blockedDevice : this.blockedDeviceList) {
                if (blockedDevice.getMessageId() == messageId && blockedDevice.getDeviceId() == deviceId && blockedDevice.getLimitId() == limitId) {
                    this.blockedDeviceList.remove(blockedDevice);
                    break;
                }
            }
        };
    }

    public Runnable deactivateLimit() {
        return () -> {
            this.limitRepository.setLimitActive(this.limit.getId(), false, Calendar.getInstance());
            this.limit = null;
        };
    }
}
