package com.example.crawler.common.implement;

import com.example.crawler.common.Parser;
import com.example.crawler.entity.Product;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserRBT implements Parser
{
    final WebClient webClient = Parser.initialiseWebClient();

    @Override
    public HtmlPage getPageProductsListStore(String productName, int waitTime) throws IOException
    {
        HtmlPage pageFirst = webClient.getPage("https://www.rbt.ru/search/?q=" + productName);

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
                    pageProductsListStore = webClient.getPage(pageProductsListStore.getUrl().toString().replace("search", "search/~/page/" + page));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                webClient.waitForBackgroundJavaScript(5000);
            }

            listElements[0] = pageProductsListStore.getByXPath("//a[@class='link link_theme_item-catalogue link_underline-color_orange link_size_b item-catalogue__item-name-link']");
            listElements[1] = pageProductsListStore.getByXPath("//div[@class='price__row price__row_current text_bold text']");

            for(int i = 0; i < listElements[0].size(); i++)
            {
                listProduct.add(new Product(listElements[0].get(i).getTextContent(), listElements[1].get(i).getTextContent().trim().replaceAll(" ","")));
            }

        }

        webClient.close();
        return listProduct;
    }

}
