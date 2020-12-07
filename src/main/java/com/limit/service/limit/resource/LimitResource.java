package com.limit.service.limit.resource;

import com.limit.service.limit.configuration.TaskTypes;
import com.limit.service.limit.repository.IBlockedDeviceRepository;
import com.limit.service.limit.model.*;
import com.limit.service.limit.repository.ILimitRepository;
import com.limit.service.limit.service.ScheduleTaskService;
import com.limit.service.limit.repository.ITicketMessageRepository;
import com.limit.service.limit.state.LimitState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class LimitResource {

    @Autowired
    private ITicketMessageRepository ticketMessageRepository;

    @Autowired
    private ILimitRepository limitRepository;

    @Autowired
    IBlockedDeviceRepository blockedDeviceRepository;

    @Autowired
    private ScheduleTaskService scheduleTaskService;

    @Autowired
    private LimitState limitState;


    @PostMapping("configure")
    public HttpStatus configureLimit(@RequestParam int time_duration, @RequestParam int stake_limit,
                                     @RequestParam int hot_percentage, @RequestParam int restriction_expires) {
        Calendar duration = Calendar.getInstance();
        duration.add(Calendar.SECOND, time_duration);

        Limit limit = new Limit(time_duration, stake_limit, hot_percentage, restriction_expires);
        Limit currLimit = this.limitState.getLimit();
        if (currLimit != null) {
            this.limitRepository.setLimitActive(currLimit.getId(), false, Calendar.getInstance());
            this.scheduleTaskService.removeTaskFromScheduler(TaskTypes.LIMIT + currLimit.getId());
        }
        limit = limitRepository.saveAndFlush(limit);
        this.limitState.setLimit(limit);
        String taskId = TaskTypes.LIMIT + limit.getId();
        this.scheduleTaskService.addTaskToScheduler(taskId, duration, limitState.deactivateLimit());
        return HttpStatus.OK;
    }

    @PostMapping("checkTicket")
    public ResponseEntity<Status> checkTicket(@RequestBody TicketMessage ticketMessage) {
        if (this.limitState.isDeviceBlocked(ticketMessage.getDeviceId())) {
            return new ResponseEntity<>(new Status("BLOCKED"), HttpStatus.OK);
        }
        Limit currLimit = this.limitState.getLimit();
        String statusMsg = "OK";
        if (currLimit != null) {
            ticketMessage.setLimitInfoId(currLimit.getId());
            double ticketsStakeSum = 0d;
            for (TicketMessage trackedTickets : this.limitState.getTrackedTicketMessages()) {
                if (trackedTickets.getDeviceId().equals(ticketMessage.getDeviceId()) && !trackedTickets.equals(ticketMessage)) {
                    ticketsStakeSum += trackedTickets.getStake();
                }
            }
            ticketsStakeSum += ticketMessage.getStake();

            double hotPercentage = currLimit.getHotPercentage();
            double hotBoundary = currLimit.getLimitValue() * (hotPercentage / 100);

            if (ticketsStakeSum > currLimit.getLimitValue()) {
                Calendar unblockAt = Calendar.getInstance();
                this.limitState.blockDevice(new BlockedDevice(ticketMessage.getDeviceId(), ticketMessage.getId(), currLimit.getId(), Calendar.getInstance(), unblockAt));
                unblockAt.add(Calendar.SECOND, currLimit.getRestrictionExpiration());
                this.ticketMessageRepository.saveAndFlush(ticketMessage);
                this.blockedDeviceRepository.saveAndFlush(new BlockedDevice(ticketMessage.getDeviceId(), ticketMessage.getId(),
                        currLimit.getId(), Calendar.getInstance(), unblockAt));
                this.scheduleTaskService.addTaskToScheduler(TaskTypes.LIMIT + ticketMessage.getDeviceId(), unblockAt,
                        this.limitState.unblockDevice(ticketMessage.getId(), ticketMessage.getDeviceId(), currLimit.getId()));
                statusMsg = "BLOCKED";
            } else if (ticketsStakeSum >= hotBoundary && ticketsStakeSum <= currLimit.getLimitValue()) {
                statusMsg = "HOT";
            }
        }
        this.limitState.addTicketMessage(ticketMessage);
        this.ticketMessageRepository.saveAndFlush(ticketMessage);
        return new ResponseEntity<>(new Status(statusMsg), HttpStatus.OK);
    }
}
