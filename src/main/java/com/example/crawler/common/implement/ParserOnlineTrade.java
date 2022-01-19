package com.example.crawler.common.implement;

import com.example.crawler.common.Parser;
import com.example.crawler.entity.Product;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ParserOnlineTrade implements Parser
{
    private final Logger log = LoggerFactory.getLogger(ParserOnlineTrade.class);

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
                pageMain = webClient.getPage("https://www.onlinetrade.ru");
            }
            catch (IOException e)
            {
                log.warn("***OnlineTrade***: Page main is not loaded " + e.getMessage());
                return listProduct;
            }

            webClient.waitForBackgroundJavaScript(waitTime);

            HtmlElement inputSearch;
            HtmlElement buttonSearch;
            try
            {
                inputSearch = (HtmlElement) pageMain.getFirstByXPath("//input[@class='header__search__inputText js__header__search__inputText']");
                buttonSearch = (HtmlElement) pageMain.getFirstByXPath("//input[@class='header__search__inputGogogo']");
                buttonSearch.removeAttribute("disabled");
                inputSearch.setAttribute("value", productName);
            }
            catch(Exception e)
            {
                log.warn("***OnlineTrade***: Element inputSearch or buttonSearch is null " + e.getMessage());
                return listProduct;
            }

            try
            {
                pageProducts = buttonSearch.click();
            }
            catch (IOException e)
            {
                log.warn("***OnlineTrade***: Page list products is not loaded " + e.getMessage());
                return listProduct;
            }

            webClient.waitForBackgroundJavaScript(waitTime);

            List<HtmlElement>[] listElements = new List[]{new ArrayList<HtmlElement>(), new ArrayList<HtmlElement>()};

            for(int page = 1; page <= countPage; page++)
            {
                if(page > 1)
                {
                    HtmlElement nextPageButton = (HtmlElement) pageProducts.getFirstByXPath("//a[@class='js__paginator__linkNext js__ajaxListingSelectPageLink js__ajaxExchange']");
                    try
                    {
                        pageProducts = nextPageButton.click();
                    }
                    catch (IOException e)
                    {
                        log.warn("***OnlineTrade***: Page list products is not loaded " + e.getMessage());
                        return listProduct;
                    }
                    webClient.waitForBackgroundJavaScript(waitTime);
                }

                try
                {
                    listElements[0] = pageProducts.getByXPath("//a[@class='indexGoods__item__name']");
                    listElements[1] = pageProducts.getByXPath("//div[@class='indexGoods__item__price']/span");
                }
                catch (Exception e)
                {
                    log.warn("***OnlineTrade***: Searched tags name or price in Xpath not find " + e.getMessage());
                    return listProduct;
                }

                for(int i = 0; i < listElements[0].size(); i++)
                {
                    listProduct.add(new Product(listElements[0].get(i).getTextContent(),listElements[1].get(i).getTextContent().trim().replace("₽", "").replaceAll(" ","")));
                }
            }

            return listProduct;
        }
        finally
        {
            closeWebClient(webClient);
        }
    }
}
