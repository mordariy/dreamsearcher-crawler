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
    LaunchParse launchParse;

    public SchedulerConfig(LaunchParse launchParse)
    {
        this.launchParse = launchParse;
    }

    //Example
    //@Scheduled(cron = "30 50 10 * * ?")
    @Scheduled(fixedRate = 90000)
    public void startParseStore()
    {
        launchParse.start("samsung", 5, 1);
    }
}
