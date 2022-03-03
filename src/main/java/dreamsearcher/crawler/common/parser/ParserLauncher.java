package dreamsearcher.crawler.common.parser;

import dreamsearcher.crawler.common.entity.Item;
import dreamsearcher.crawler.common.entity.Run;
import dreamsearcher.crawler.common.service.ItemService;
import dreamsearcher.crawler.common.service.RunService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class ParserLauncher {
    private final List<Parser> parser;
    private final Logger log = LoggerFactory.getLogger(ParserLauncher.class);
    private final ItemService itemService;
    private final RunService runService;

    public ParserLauncher(List<Parser> parser, ItemService itemService, RunService runService) {
        this.parser = parser;
        this.itemService = itemService;
        this.runService = runService;
    }

    public void start(String productName, int waitTimeInSec, int countPageToParse) {
        log.info("[Crawler] Parsing has started with the specified parameters: productName = {}, waitTimeInSec = {}, countPageToParse = {}",
                productName, waitTimeInSec, countPageToParse);

        List<CompletableFuture<List<Item>>> listFuture = asyncParsePages(productName, waitTimeInSec, countPageToParse);
        asyncSaveItems(listFuture);
    }

    private List<CompletableFuture<List<Item>>> asyncParsePages(String productName, int waitTimeInSec, int countPageToParse) {
        CompletableFuture<List<Item>>[] com = new CompletableFuture[parser.size()];
        List<CompletableFuture<List<Item>>> listFuture = new ArrayList<>();
        for (int i = 0; i < parser.size(); i++) {
            int finalI = i;
            long duration = System.currentTimeMillis();
            log.info("[Crawler] Parsing starts in {}", parser.get(i).getShop());

            log.info("[Crawler] Creating and saving Run...");
            Run run = runService.create(parser.get(i).getShop());
            com[i] = CompletableFuture.supplyAsync(() -> parser.get(finalI).parsePages(productName, waitTimeInSec * 1000, countPageToParse, run));
            listFuture.add(com[finalI]);
            run.setProcessed(false);
            runService.update(run);

            duration = System.currentTimeMillis() - duration;
            log.info("[Crawler] Parsing in {} ends for {} milliseconds", parser.get(i).getShop(), duration);
        }
        return listFuture;
    }

    private void asyncSaveItems(List<CompletableFuture<List<Item>>> listFuture) {
        CompletableFuture<Void> result = CompletableFuture.allOf(listFuture.toArray(new CompletableFuture[]{}));
        result.thenRunAsync(() ->
        {
            try {
                log.info("[Crawler] Saving items in DB. . .");
                for (int i = 0; i < listFuture.size(); i++) {
                    log.info("[Crawler] Saving: {}th circle start", i);
                    List<Item> savedItems = itemService.saveItems(listFuture.get(i).get());
                    if (savedItems.size() == 0) log.warn("[Crawler] Nothing has been saved...");
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
