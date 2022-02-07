package dreamsearcher.crawler.common;

import dreamsearcher.crawler.common.entity.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class ParserLauncher {
    List<Parser> parser;
    private final Logger log = LoggerFactory.getLogger(ParserLauncher.class);

    public ParserLauncher(List<Parser> parser) {
        this.parser = parser;
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
                //Тут должна быть запись в бд
                System.out.println("----------Citilink----------");
                for (Item item : listFuture.get(0).get()) {
                    System.out.println("Название товара: " + item.getItemName() + " цена: " + item.getPrice());
                }
                System.out.println("----------COMPASS----------");
                for (Item item : listFuture.get(1).get()) {
                    System.out.println("Название товара: " + item.getItemName() + " цена: " + item.getPrice());
                }
                System.out.println("----------OnlineTrade----------");
                for (Item item : listFuture.get(2).get()) {
                    System.out.println("Название товара: " + item.getItemName() + " цена: " + item.getPrice());
                }
                System.out.println("----------RBT----------");
                for (Item item : listFuture.get(3).get()) {
                    System.out.println("Название товара: " + item.getItemName() + " цена: " + item.getPrice());
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
