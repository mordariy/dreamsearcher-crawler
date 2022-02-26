package dreamsearcher.crawler.common.parser.impl;

import dreamsearcher.crawler.common.entity.Item;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import dreamsearcher.crawler.common.entity.Run;
import dreamsearcher.crawler.common.parser.Parser;
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
    public List<Item> parsePages(String productName, int waitTime, int countPage, Run run) {
        List<Item> listItem = new ArrayList<>();

        try (WebClient webClient = initialiseWebClient()) {
            HtmlPage mainPage = null;
            HtmlPage page = null;

            try {
                mainPage = webClient.getPage("https://www.onlinetrade.ru");
            } catch (IOException e) {
                log.warn("[Crawler] ***OnlineTrade***: Page main is not loaded " + e.getMessage());
                return listItem;
            }

            webClient.waitForBackgroundJavaScript(waitTime);

            HtmlElement inputSearch;
            HtmlElement buttonSearch;
            try {
                inputSearch = (HtmlElement) mainPage.getFirstByXPath("//input[@class='header__search__inputText js__header__search__inputText']");
                buttonSearch = (HtmlElement) mainPage.getFirstByXPath("//input[@class='header__search__inputGogogo']");
                buttonSearch.removeAttribute("disabled");
                inputSearch.setAttribute("value", productName);
            } catch (Exception e) {
                log.warn("[Crawler] ***OnlineTrade***: Element inputSearch or buttonSearch is null " + e.getMessage());
                return listItem;
            }

            try {
                page = buttonSearch.click();
            } catch (IOException e) {
                log.warn("[Crawler] ***OnlineTrade***: Page list products is not loaded " + e.getMessage());
                return listItem;
            }

            webClient.waitForBackgroundJavaScript(waitTime);

            List<HtmlElement>[] listElements = new List[]{new ArrayList<HtmlElement>(), new ArrayList<HtmlElement>()};

            for (int pageNum = 1; pageNum <= countPage; pageNum++) {
                if (pageNum > 1) {
                    HtmlElement nextPageButton = (HtmlElement) page.getFirstByXPath("//a[@class='js__paginator__linkNext js__ajaxListingSelectPageLink js__ajaxExchange']");
                    try {
                        page = nextPageButton.click();
                    } catch (IOException e) {
                        log.warn("[Crawler] ***OnlineTrade***: Page list products is not loaded " + e.getMessage());
                        return listItem;
                    }
                    webClient.waitForBackgroundJavaScript(waitTime);
                }

                try {
                    listElements[0] = page.getByXPath("//a[@class='indexGoods__item__name']");
                    listElements[1] = page.getByXPath("//div[@class='indexGoods__item__price']/span");
                } catch (Exception e) {
                    log.warn("[Crawler] ***OnlineTrade***: Searched tags name or price in Xpath not find " + e.getMessage());
                    return listItem;
                }

                for (int i = 0; i < listElements[0].size(); i++) {
                    listItem.add(Item.builder()
                            .runId(run.getRunId())
                            .itemName(listElements[0].get(i).getTextContent())
                            .price(Double.parseDouble(listElements[1].get(i).getTextContent().trim().replaceAll("[^0-9]", "").replaceAll(" ", "")))
                            .itemName(productName)
                            .link(page.getUrl().toString()) //Сейчас ссылка общая по типу "https://www.citilink.ru/search/?text=iphone12&p=1". todo: сделать конкретную ссылку на Item по типу "https://www.citilink.ru/product/smartfon-apple-iphone-12-mgja3ru-a-chernyi-1428565/"
                            .build());
                }
            }

            return listItem;
        }
    }

    @Override
    public String getShop() {
        return "OnlineTrade";
    }
}
