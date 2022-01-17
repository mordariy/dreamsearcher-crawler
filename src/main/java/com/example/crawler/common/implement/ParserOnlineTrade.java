package com.example.crawler.common.implement;

import com.example.crawler.common.Parser;
import com.example.crawler.entity.Product;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserOnlineTrade implements Parser
{
    final WebClient webClient = Parser.initialiseWebClient();

    @Override
    public HtmlPage getPageProductsListStore(String productName, int waitTime) throws IOException
    {
        if(webClient == null)
        {
            return null;
        }

        HtmlPage pageFirst = webClient.getPage("https://www.onlinetrade.ru");

        webClient.waitForBackgroundJavaScript(waitTime);

        HtmlElement inputSearch = (HtmlElement) pageFirst.getFirstByXPath("//input[@class='header__search__inputText js__header__search__inputText']");
        HtmlElement buttonSearch = (HtmlElement) pageFirst.getFirstByXPath("//input[@class='header__search__inputGogogo']");
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

        for(int page = 1; page <= countPage; page++)
        {
            if(page > 1)
            {
                HtmlElement nextPageButton = (HtmlElement) pageProductsListStore.getFirstByXPath("//a[@class='js__paginator__linkNext js__ajaxListingSelectPageLink js__ajaxExchange']");
                try
                {
                    pageProductsListStore = nextPageButton.click();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                webClient.waitForBackgroundJavaScript(5000);
            }

            listElements[0] = pageProductsListStore.getByXPath("//a[@class='indexGoods__item__name']");
            listElements[1] = pageProductsListStore.getByXPath("//div[@class='indexGoods__item__price']/span");

            for(int i = 0; i < listElements[0].size(); i++)
            {
                listProduct.add(new Product(listElements[0].get(i).getTextContent(),listElements[1].get(i).getTextContent().trim().replace("₽", "").replaceAll(" ","")));
            }
        }

        webClient.close();
        return listProduct;
    }
}
