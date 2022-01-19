package com.example.crawler.common.implement;

import com.example.crawler.common.Parser;
import com.example.crawler.entity.Product;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class ParserCompass implements Parser
{
    private final Logger log = LoggerFactory.getLogger(ParserCompass.class);

    @Override
    public List<Product> parsePages(String productName, int waitTime, int countPage)
    {
        List<Product> listProduct = new ArrayList<Product>();
        final WebClient webClient = initialiseWebClient();

        try(webClient)
        {
            HtmlPage pageMain = null;
            HtmlPage pageProducts = null;

            try
            {
                pageMain = webClient.getPage("https://www.compass.com.ru");
            }
            catch (IOException e)
            {
                log.warn("***Compass***: Page main is not loaded " + e.getMessage());
                return listProduct;
            }

            webClient.waitForBackgroundJavaScript(waitTime);

            HtmlElement inputSearch;
            HtmlElement buttonSearch;
            try
            {
                inputSearch = (HtmlElement) pageMain.getFirstByXPath("//input[@id='searchText']");
                buttonSearch = (HtmlElement) pageMain.getFirstByXPath("//input[@id='searchSubmit']");
                buttonSearch.removeAttribute("disabled");
                inputSearch.setAttribute("value", productName);
            }
            catch(Exception e)
            {
                log.warn("***Compass***: Element inputSearch or buttonSearch is null " + e.getMessage());
                return listProduct;
            }

            try
            {
                pageProducts = buttonSearch.click();
            }
            catch (IOException e)
            {
                log.warn("***Compass***: Page list products is not loaded " + e.getMessage());
                return listProduct;
            }

            List<HtmlElement>[] listElements = new List[]{new ArrayList<HtmlElement>(), new ArrayList<HtmlElement>()};

            webClient.waitForBackgroundJavaScript(waitTime);

            try
            {
                listElements[0] = pageProducts.getByXPath("//td[@class='searchPgroup mobileHide']/following-sibling::td[1]/a");
                listElements[1] = pageProducts.getByXPath("//div[@class='price']");
            }
            catch (Exception e)
            {
                log.warn("***Compass***: Searched tags name or price in Xpath not find " + e.getMessage());
                return listProduct;
            }

            for(int i = 0; i < listElements[0].size(); i++)
            {
                listProduct.add(new Product(listElements[0].get(i).getTextContent(),listElements[1].get(i).getTextContent().trim().replaceAll(" ","")));
            }

            return listProduct;
        }
        finally
        {
            closeWebClient(webClient);
        }

    }
}
