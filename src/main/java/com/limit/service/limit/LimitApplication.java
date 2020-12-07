package com.limit.service.limit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class LimitApplication {

    public static void main(String[] args) {
        SpringApplication.run(LimitApplication.class, args);
    }

    @Configuration
    @EnableScheduling
    @ConditionalOnProperty(name = "scheduling.enabled", matchIfMissing = true)
    class SchedulingConfiguration {

    }
}
