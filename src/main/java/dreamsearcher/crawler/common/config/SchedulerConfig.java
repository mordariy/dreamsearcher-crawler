package dreamsearcher.crawler.common.config;

import dreamsearcher.crawler.common.ParserLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.ExecutionException;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class SchedulerConfig {
    private final ParserLauncher parserLauncher;
    private final Logger log = LoggerFactory.getLogger(SchedulerConfig.class);

    public SchedulerConfig(ParserLauncher parserLauncher) {
        this.parserLauncher = parserLauncher;
    }

    //todo: make a dynamic startup time to be unpredictable for the anti-bot
    //todo: remove hardcode and make like "@Scheduled(cron = "${cron.expression}")"
    //Раз в день в 10:50 и 30 секунд
    //@Scheduled(cron = "30 50 10 * * ?")
    //Для теста, каждые 90 сек
    @Scheduled(fixedRate = 10000)
    public void startParseStore() throws ExecutionException, InterruptedException {
        long duration = System.currentTimeMillis();
        log.info("[Crawler] The parsing started according to the set schedule");
        parserLauncher.start("samsung", 5, 1);
        duration = System.currentTimeMillis() - duration;
        log.info("[Crawler] Parsing completed in {} milliseconds", duration);
    }
}
