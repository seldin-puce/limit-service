package com.limit.service.limit.service;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
public class ScheduleTaskService {

    TaskScheduler scheduler;

    Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();

    public ScheduleTaskService(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void addTaskToScheduler(String id, Calendar triggerAt, Runnable task) {
        ScheduledFuture<?> scheduledTask = scheduler.schedule(task, triggerAt.getTime());
        jobsMap.put(id, scheduledTask);
    }

    public void removeTaskFromScheduler(String id) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(id);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.put(id, null);
        }
    }

    @EventListener({ContextRefreshedEvent.class})
    void contextRefreshedEvent() {
    }
}