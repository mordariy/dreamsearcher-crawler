package com.example.crawler.common.implement;

import com.example.crawler.common.Parser;
import com.example.crawler.entity.Product;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserCompass implements Parser
{
    final WebClient webClient = Parser.initialiseWebClient();

    @Override
    public HtmlPage getPageProductsListStore(String productName, int waitTime) throws IOException
    {
        if(webClient == null)
        {
            return null;
        }

        HtmlPage pageFirst = webClient.getPage("https://www.compass.com.ru");

        webClient.waitForBackgroundJavaScript(waitTime);

        HtmlElement inputSearch = (HtmlElement) pageFirst.getFirstByXPath("//input[@id='searchText']");
        HtmlElement buttonSearch = (HtmlElement) pageFirst.getFirstByXPath("//input[@id='searchSubmit']");
        buttonSearch.removeAttribute("disabled");

        inputSearch.setAttribute("value",productName);

        HtmlPage pageSecond = buttonSearch.click();
        //webClient.waitForBackgroundJavaScript(waitTime);

        return pageSecond;
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

        listElements[0] = pageProductsListStore.getByXPath("//td[@class='searchPgroup mobileHide']/following-sibling::td[1]/a");
        listElements[1] = pageProductsListStore.getByXPath("//div[@class='price']");

        for(int i = 0; i < listElements[0].size(); i++)
        {
            listProduct.add(new Product(listElements[0].get(i).getTextContent(),listElements[1].get(i).getTextContent().trim().replaceAll(" ","")));
        }

        webClient.close();
        return listProduct;
    }
}
