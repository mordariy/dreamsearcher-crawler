package com.example.crawler.config;

import com.example.crawler.common.LaunchParse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.ExecutionException;

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

    //Раз в день в 10:50 и 30 секунд
    //@Scheduled(cron = "30 50 10 * * ?")
    //Для теста, каждые 90 сек
    @Scheduled(fixedRate = 90000)
    public void startParseStore() throws ExecutionException, InterruptedException
    {
        launchParse.start("samsung", 5, 1);
    }
}
