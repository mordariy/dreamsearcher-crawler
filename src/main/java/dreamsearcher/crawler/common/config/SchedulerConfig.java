package dreamsearcher.crawler.common.config;

import dreamsearcher.crawler.common.parser.ParserLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    //Раз в день в 10:50 и 30 секунд
    //@Scheduled(cron = "30 50 10 * * ?")
    //Для теста, каждые fixedRate/1000 сек
    @Scheduled(fixedRate = 1000000000)
    public void startParseStore() throws ExecutionException, InterruptedException {
        long duration = System.currentTimeMillis();
        log.info("[Crawler] The parsing started according to the set schedule");

        createItemsToFind().forEach(item -> parserLauncher.start(item, 5, 1));

        duration = System.currentTimeMillis() - duration;
        log.info("[Crawler] Parsing completed in {} milliseconds", duration);
    }

    private List<String> createItemsToFind() {
        List<String> items = new ArrayList<>();
        items.add("Xiaomi Poco X3 Pro");
        items.add("DeLonghi Magnifica ECAM22.110.B");
        items.add("Xiaomi Mi Robot Vacuum Mop Essential");
        items.add("SAMSUNG UE50AU7500UXRU");
        items.add("NVIDIA GeForce GTX 1660TI");

        return items;
    }
}
