package com.example.crawler.common;

import com.example.crawler.common.Parser;
import com.example.crawler.common.implement.ParserCitilink;
import com.example.crawler.common.implement.ParserCompass;
import com.example.crawler.common.implement.ParserOnlineTrade;
import com.example.crawler.common.implement.ParserRBT;
import com.example.crawler.entity.Product;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class LaunchParse
{
    public static void start()
    {
        final List<Product>[] list = new List[]{new ArrayList<Product>(), new ArrayList<Product>(), new ArrayList<Product>(), new ArrayList<Product>()};
        Parser[] parser = {new ParserRBT(), new ParserCompass(), new ParserOnlineTrade(), new ParserCitilink()};

        CompletableFuture<List> com1 = CompletableFuture.supplyAsync(()->
        {
            try
            {
                list[0] = parser[0].parsePages(parser[0].getPageProductsListStore("samsung", 5000), 2);
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            return list[0];
        });
        CompletableFuture<List> com2 = CompletableFuture.supplyAsync(()->
        {
            try
            {
                list[1] = parser[1].parsePages(parser[1].getPageProductsListStore("флешка", 5000), 0);
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            return list[1];
        });
        CompletableFuture<List> com3 = CompletableFuture.supplyAsync(()->
        {
            try
            {
                list[2] = parser[2].parsePages(parser[2].getPageProductsListStore("ps5", 5000), 2);
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            return list[2];
        });
        CompletableFuture<List> com4 = CompletableFuture.supplyAsync(()->
        {
            try
            {
                list[3] = parser[3].parsePages(parser[3].getPageProductsListStore("наушники sony", 5000), 2);
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            return list[3];
        });

        List<CompletableFuture<List>> listFuture = new ArrayList<>();
        listFuture.add(com1);
        listFuture.add(com2);
        listFuture.add(com3);
        listFuture.add(com4);

        CompletableFuture<Void> result = CompletableFuture.allOf(listFuture.toArray(new CompletableFuture[]{}));
        result.thenRunAsync(() ->
        {
            System.out.println("----------RBT----------");
            for(Product item : list[0])
            {
                System.out.println("Название товара: " + item.getName() + " цена: " + item.getPrice());
            }
            System.out.println("----------COMPASS----------");
            for(Product item : list[1])
            {
                System.out.println("Название товара: " + item.getName() + " цена: " + item.getPrice());
            }
            System.out.println("----------OnlineTrade----------");
            for(Product item : list[2])
            {
                System.out.println("Название товара: " + item.getName() + " цена: " + item.getPrice());
            }
            System.out.println("----------Citilink----------");
            for(Product item : list[3])
            {
                System.out.println("Название товара: " + item.getName() + " цена: " + item.getPrice());
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
