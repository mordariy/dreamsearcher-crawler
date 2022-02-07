package dreamsearcher.crawler.common.implement;

import dreamsearcher.crawler.common.Parser;
import dreamsearcher.crawler.common.entity.Item;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ParserCompass implements Parser {
    private final Logger log = LoggerFactory.getLogger(ParserCompass.class);

    @Override
    public List<Item> parsePages(String productName, int waitTime, int countPage) {
        List<Item> listItem = new ArrayList<>();

        try (WebClient webClient = initialiseWebClient()) {
            HtmlPage pageMain = null;
            HtmlPage pageProducts = null;

            try {
                //todo: Remove this hardcode, create config for shops urls or anything else
                pageMain = webClient.getPage("https://www.compass.com.ru");
            } catch (IOException e) {
                log.warn("[Crawler] ***Compass***: Page main is not loaded " + e.getMessage());
                return listItem;
            }

            webClient.waitForBackgroundJavaScript(waitTime);

            HtmlElement inputSearch;
            HtmlElement buttonSearch;
            try {
                inputSearch = (HtmlElement) pageMain.getFirstByXPath("//input[@id='searchText']");
                buttonSearch = (HtmlElement) pageMain.getFirstByXPath("//input[@id='searchSubmit']");
                buttonSearch.removeAttribute("disabled");
                inputSearch.setAttribute("value", productName);
            } catch (Exception e) {
                log.warn("[Crawler] ***Compass***: Element inputSearch or buttonSearch is null " + e.getMessage());
                return listItem;
            }

            try {
                pageProducts = buttonSearch.click();
            } catch (IOException e) {
                log.warn("[Crawler] ***Compass***: Page list products is not loaded " + e.getMessage());
                return listItem;
            }

            List<HtmlElement>[] listElements = new List[]{new ArrayList<HtmlElement>(), new ArrayList<HtmlElement>()};

            webClient.waitForBackgroundJavaScript(waitTime);

            try {
                listElements[0] = pageProducts.getByXPath("//td[@class='searchPgroup mobileHide']/following-sibling::td[1]/a");
                listElements[1] = pageProducts.getByXPath("//div[@class='price']");
            } catch (Exception e) {
                log.warn("[Crawler] ***Compass***: Searched tags name or price in Xpath not find " + e.getMessage());
                return listItem;
            }

            for (int i = 0; i < listElements[0].size(); i++) {
                listItem.add(new Item(listElements[0].get(i).getTextContent(), listElements[1].get(i).getTextContent().trim().replaceAll(" ", "")));
            }

            return listItem;
        }

    }
}
