package com.example.crawler.common.implement;

import com.example.crawler.common.Parser;
import com.example.crawler.entity.Product;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserCitilink implements Parser
{
    final WebClient webClient = Parser.initialiseWebClient();

    @Override
    public HtmlPage getPageProductsListStore(String productName, int waitTime) throws IOException
    {
        HtmlPage pageFirst = webClient.getPage("https://www.citilink.ru/search/?text=" + productName);

        webClient.waitForBackgroundJavaScript(waitTime);

        return pageFirst;
    }

    @Override
    public List<Product> parsePages(HtmlPage pageProductsListStore, int countPage)
    {
        if(pageProductsListStore == null)
        {
            return null;
        }

        List<HtmlElement>[] listElements = new List[]{new ArrayList<HtmlElement>(), new ArrayList<HtmlElement>()};
        List<Product> listProduct = new ArrayList<Product>();

        for(int page = 1; page <= countPage; page++)
        {
            if(page > 1)
            {
                try
                {
                    pageProductsListStore = webClient.getPage(pageProductsListStore.getUrl() + "&p=" + page);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                webClient.waitForBackgroundJavaScript(5000);
            }

            listElements[0] = pageProductsListStore.getByXPath("//a[@class=' ProductCardVertical__name  Link js--Link Link_type_default']");
            listElements[1] = pageProductsListStore.getByXPath("//span[@class='ProductCardVerticalPrice__price-current_current-price js--ProductCardVerticalPrice__price-current_current-price ']");

            for(int i = 0; i < listElements[0].size(); i++)
            {
                //i+i это номер элемента цены товара, так как на сайте присутсвует по два элемента цены товара
                //Ограничение, чтобы не попадали товары, которые не присутствуют на складе
                if(i+i < listElements[1].size())
                {
                    listProduct.add(new Product(listElements[0].get(i).getTextContent(),listElements[1].get(i+i).getTextContent().trim().replaceAll(" ","")));
                }

            }

        }

        webClient.close();
        return listProduct;
    }
}
