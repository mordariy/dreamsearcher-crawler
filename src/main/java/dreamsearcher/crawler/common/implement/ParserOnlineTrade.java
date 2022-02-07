package dreamsearcher.crawler.common.implement;

import dreamsearcher.crawler.common.Parser;
import dreamsearcher.crawler.common.entity.Item;
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
public class ParserOnlineTrade implements Parser {
    private final Logger log = LoggerFactory.getLogger(ParserOnlineTrade.class);

    @Override
    public List<Item> parsePages(String productName, int waitTime, int countPage) {
        List<Item> listItem = new ArrayList<>();

        try (WebClient webClient = initialiseWebClient()) {
            HtmlPage pageMain = null;
            HtmlPage pageProducts = null;

            try {
                //todo: Remove this hardcode, create config for shops urls
                pageMain = webClient.getPage("https://www.onlinetrade.ru");
            } catch (IOException e) {
                log.warn("[Crawler] ***OnlineTrade***: Page main is not loaded " + e.getMessage());
                return listItem;
            }

            webClient.waitForBackgroundJavaScript(waitTime);

            HtmlElement inputSearch;
            HtmlElement buttonSearch;
            try {
                inputSearch = (HtmlElement) pageMain.getFirstByXPath("//input[@class='header__search__inputText js__header__search__inputText']");
                buttonSearch = (HtmlElement) pageMain.getFirstByXPath("//input[@class='header__search__inputGogogo']");
                buttonSearch.removeAttribute("disabled");
                inputSearch.setAttribute("value", productName);
            } catch (Exception e) {
                log.warn("[Crawler] ***OnlineTrade***: Element inputSearch or buttonSearch is null " + e.getMessage());
                return listItem;
            }

            try {
                pageProducts = buttonSearch.click();
            } catch (IOException e) {
                log.warn("[Crawler] ***OnlineTrade***: Page list products is not loaded " + e.getMessage());
                return listItem;
            }

            webClient.waitForBackgroundJavaScript(waitTime);

            List<HtmlElement>[] listElements = new List[]{new ArrayList<HtmlElement>(), new ArrayList<HtmlElement>()};

            for (int page = 1; page <= countPage; page++) {
                if (page > 1) {
                    HtmlElement nextPageButton = (HtmlElement) pageProducts.getFirstByXPath("//a[@class='js__paginator__linkNext js__ajaxListingSelectPageLink js__ajaxExchange']");
                    try {
                        pageProducts = nextPageButton.click();
                    } catch (IOException e) {
                        log.warn("[Crawler] ***OnlineTrade***: Page list products is not loaded " + e.getMessage());
                        return listItem;
                    }
                    webClient.waitForBackgroundJavaScript(waitTime);
                }

                try {
                    listElements[0] = pageProducts.getByXPath("//a[@class='indexGoods__item__name']");
                    listElements[1] = pageProducts.getByXPath("//div[@class='indexGoods__item__price']/span");
                } catch (Exception e) {
                    log.warn("[Crawler] ***OnlineTrade***: Searched tags name or price in Xpath not find " + e.getMessage());
                    return listItem;
                }

                for (int i = 0; i < listElements[0].size(); i++) {
                    listItem.add(new Item(listElements[0].get(i).getTextContent(), listElements[1].get(i).getTextContent().trim().replace("₽", "").replaceAll(" ", "")));
                }
            }

            return listItem;
        }
    }
}
