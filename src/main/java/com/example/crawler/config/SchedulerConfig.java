package com.example.crawler.config;

import com.example.crawler.common.LaunchParse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class SchedulerConfig
{
    //Example
    @Scheduled(cron = "35 23 10 * * ?")
    public void startParseStore()
    {
        LaunchParse.start();
    }
}
