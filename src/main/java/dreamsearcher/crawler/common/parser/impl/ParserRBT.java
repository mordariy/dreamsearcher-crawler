package dreamsearcher.crawler.common.parser.impl;

import dreamsearcher.crawler.common.entity.Item;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import dreamsearcher.crawler.common.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ParserRBT implements Parser {
    private final Logger log = LoggerFactory.getLogger(ParserRBT.class);

    @Override
    public List<Item> parsePages(String productName, int waitTime, int countPage) {
        List<Item> listItem = new ArrayList<>();

        try (WebClient webClient = initialiseWebClient()) {
            HtmlPage pageProducts;

            try {
                pageProducts = webClient.getPage("https://www.rbt.ru/search/?search_provider=anyquery&strategy=vectors_extended,zero_queries&q=" + productName);
            } catch (IOException e) {
                log.warn("[Crawler] ***RBT***: Page list products is not loaded " + e.getMessage());
                return listItem;
            }

            webClient.waitForBackgroundJavaScript(waitTime);

            List<HtmlElement>[] listElements = new List[]{new ArrayList<HtmlElement>(), new ArrayList<HtmlElement>()};

            for (int page = 1; page <= countPage; page++) {
                if (page > 1) {
                    try {
                        pageProducts = webClient.getPage(pageProducts.getUrl().toString().replace("search", "search/~/page/" + page));
                    } catch (IOException e) {
                        log.warn("[Crawler] ***RBT***: Page list products is not loaded " + e.getMessage());
                        return listItem;
                    }
                    webClient.waitForBackgroundJavaScript(waitTime);
                }

                try {
                    listElements[0] = pageProducts.getByXPath("//a[@class='link link_theme_item-catalogue link_underline-color_orange link_size_b item-catalogue__item-name-link']");
                    listElements[1] = pageProducts.getByXPath("//div[@class='price__row price__row_current text_bold text']");
                } catch (Exception e) {
                    log.warn("[Crawler] ***RBT***: Searched tags name or price in Xpath not find " + e.getMessage());
                    return listItem;
                }

                for (int i = 0; i < listElements[0].size(); i++) {
                    //listItem.add(new Item(listElements[0].get(i).getTextContent(), listElements[1].get(i).getTextContent().trim().replaceAll(" ", "")));
                }
            }

            return listItem;

        }
    }

    @Override
    public String getShop() {
        return "RBT";
    }

}
