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
public class ParserRBT implements Parser
{
    private final Logger log = LoggerFactory.getLogger(ParserRBT.class);

    @Override
    public List<Product> parsePages(String productName, int waitTime, int countPage)
    {
        List<Product> listProduct = new ArrayList<Product>();
        final WebClient webClient = initialiseWebClient();

        try(webClient)
        {
            HtmlPage pageProducts;

            try
            {
                pageProducts = webClient.getPage("https://www.rbt.ru/search/?search_provider=anyquery&strategy=vectors_extended,zero_queries&q=" + productName);
            }
            catch (IOException e)
            {
                log.warn("***RBT***: Page list products is not loaded " + e.getMessage());
                return listProduct;
            }

            webClient.waitForBackgroundJavaScript(waitTime);

            List<HtmlElement>[] listElements = new List[]{new ArrayList<HtmlElement>(), new ArrayList<HtmlElement>()};

            for (int page = 1; page <= countPage; page++)
            {
                if (page > 1)
                {
                    try
                    {
                        pageProducts = webClient.getPage(pageProducts.getUrl().toString().replace("search", "search/~/page/" + page));
                    }
                    catch (IOException e)
                    {
                        log.warn("***RBT***: Page list products is not loaded " + e.getMessage());
                        return listProduct;
                    }
                    webClient.waitForBackgroundJavaScript(waitTime);
                }

                try
                {
                    listElements[0] = pageProducts.getByXPath("//a[@class='link link_theme_item-catalogue link_underline-color_orange link_size_b item-catalogue__item-name-link']");
                    listElements[1] = pageProducts.getByXPath("//div[@class='price__row price__row_current text_bold text']");
                }
                catch(Exception e)
                {
                    log.warn("***RBT***: Searched tags name or price in Xpath not find " + e.getMessage());
                    return listProduct;
                }

                for (int i = 0; i < listElements[0].size(); i++)
                {
                    listProduct.add(new Product(listElements[0].get(i).getTextContent(), listElements[1].get(i).getTextContent().trim().replaceAll(" ", "")));
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
