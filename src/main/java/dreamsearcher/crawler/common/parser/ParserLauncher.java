package dreamsearcher.crawler.common.parser;

import dreamsearcher.crawler.common.entity.Item;
import dreamsearcher.crawler.common.entity.Run;
import dreamsearcher.crawler.common.service.ItemService;
import dreamsearcher.crawler.common.service.RunService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class ParserLauncher {
    List<Parser> parser;
    private final Logger log = LoggerFactory.getLogger(ParserLauncher.class);
    private final ItemService itemService;
    private final RunService runService;

    public ParserLauncher(List<Parser> parser, ItemService itemService, RunService runService) {
        this.parser = parser;
        this.itemService = itemService;
        this.runService = runService;
    }

    public void start(String productName, int waitTimeInSec, int countPageToParse) {
        CompletableFuture<List<Item>>[] com = new CompletableFuture[parser.size()];
        List<CompletableFuture<List<Item>>> listFuture = new ArrayList<>();
        log.info("[Crawler] Parsing has started with the specified parameters: productName = {}, waitTimeInSec = {}, countPageToParse = {}",
                productName, waitTimeInSec, countPageToParse);

        for (int i = 0; i < parser.size(); i++) {
            int finalI = i;
            long duration = System.currentTimeMillis();
            log.info("[Crawler] The {}th parsing circle starts", i);
            com[i] = CompletableFuture.supplyAsync(() ->
            {
                return parser.get(finalI).parsePages(productName, waitTimeInSec * 1000, countPageToParse);
            });
            duration = System.currentTimeMillis() - duration;
            listFuture.add(com[finalI]);
            log.info("[Crawler] The {}th parsing circle ends for {} milliseconds", i, duration);
        }

        CompletableFuture<Void> result = CompletableFuture.allOf(listFuture.toArray(new CompletableFuture[]{}));
        result.thenRunAsync(() ->
        {
            try {
                log.info("[Crawler] Saving items in DB. . .");
                for (int i = 0; i < listFuture.size(); i++) {
                    log.info("[Crawler] Saving: {}th circle start", i);
                    itemService.saveItems(listFuture.get(i).get());
                    log.info("[Crawler] Saving: {}th circle end", i);
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        });

        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
