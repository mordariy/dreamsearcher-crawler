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

    public void start(String productName, int waitTimeInSec, int countPageToParse) throws InterruptedException, ExecutionException
    {
        CompletableFuture<List<Product>>[] com = new CompletableFuture[parser.size()];
        List<CompletableFuture<List<Product>>> listFuture = new ArrayList<>();

        for(int i = 0; i < parser.size(); i++)
        {
            int finalI = i;
            com[i] = CompletableFuture.supplyAsync(()->
            {
                return parser.get(finalI).parsePages(productName, waitTimeInSec * 1000, countPageToParse);
            });
            listFuture.add(com[finalI]);
        }

        CompletableFuture<Void> result = CompletableFuture.allOf(listFuture.toArray(new CompletableFuture[]{}));
        result.thenRunAsync(() ->
        {
            try
            {
                //Тут должна быть запись в бд
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
            }
            catch (InterruptedException | ExecutionException e)
            {
                throw new RuntimeException(e.getMessage(),  e);
            }
        });

        try
        {
            result.get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            throw new RuntimeException(e.getMessage(),  e);
        }
    }
}
