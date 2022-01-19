package com.example.crawler.common;

import com.example.crawler.entity.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class LaunchParse
{
    List<Parser> parser;

    public LaunchParse(List<Parser> parser)
    {
        this.parser = parser;
    }

    public void start(String productName, int waitTimeInSec, int countPageToParse)
    {
        CompletableFuture<List<Product>> com1 = CompletableFuture.supplyAsync(()->
        {
            return parser.get(0).parsePages(productName, waitTimeInSec * 1000, countPageToParse);
        });
        CompletableFuture<List<Product>> com2 = CompletableFuture.supplyAsync(()->
        {
            return parser.get(1).parsePages(productName, waitTimeInSec * 1000, countPageToParse);
        });
        CompletableFuture<List<Product>> com3 = CompletableFuture.supplyAsync(()->
        {
            return parser.get(2).parsePages(productName, waitTimeInSec * 1000, countPageToParse);
        });
        CompletableFuture<List<Product>> com4 = CompletableFuture.supplyAsync(()->
        {
            return parser.get(3).parsePages(productName, waitTimeInSec * 1000, countPageToParse);
        });

        List<CompletableFuture<List<Product>>> listFuture = new ArrayList<>();
        listFuture.add(com1);
        listFuture.add(com2);
        listFuture.add(com3);
        listFuture.add(com4);

        CompletableFuture<Void> result = CompletableFuture.allOf(listFuture.toArray(new CompletableFuture[]{}));
        result.thenRunAsync(() ->
        {
            try
            {
                System.out.println("----------Citilink----------");
                for(Product item : listFuture.get(0).get())
                {
                    System.out.println("Название товара: " + item.getName() + " цена: " + item.getPrice());
                }
                System.out.println("----------COMPASS----------");
                for(Product item : listFuture.get(1).get())
                {
                    System.out.println("Название товара: " + item.getName() + " цена: " + item.getPrice());
                }
                System.out.println("----------OnlineTrade----------");
                for(Product item : listFuture.get(2).get())
                {
                    System.out.println("Название товара: " + item.getName() + " цена: " + item.getPrice());
                }
                System.out.println("----------RBT----------");
                for(Product item : listFuture.get(3).get())
                {
                    System.out.println("Название товара: " + item.getName() + " цена: " + item.getPrice());
                }
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        });

        try
        {
            result.get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
    }
}
