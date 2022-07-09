package com.daangndaangn.apiserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "scheduler.thread")
public class SchedulerConfig {

    private String threadNamePrefix;
    private int poolSize;

    @Bean
    public TaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix(threadNamePrefix);
        scheduler.setPoolSize(poolSize);
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.afterPropertiesSet();
        return scheduler;
    }
}
