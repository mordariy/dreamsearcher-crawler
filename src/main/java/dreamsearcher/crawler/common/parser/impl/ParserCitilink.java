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
public class ParserCitilink implements Parser {
    private final Logger log = LoggerFactory.getLogger(ParserCitilink.class);

    @Override
    public List<Item> parsePages(String productName, int waitTime, int countPage, Run run) {
        List<Item> listItem = new ArrayList<>();

        try (WebClient webClient = initialiseWebClient()) {
            HtmlPage page;

            try {
                page = webClient.getPage("https://www.citilink.ru/search/?text=" + productName);
            } catch (IOException e) {
                log.warn("[Crawler] ***Citilink***: Page list products is not loaded " + e.getMessage());
                return listItem;
            }

            webClient.waitForBackgroundJavaScript(waitTime);

            List<HtmlElement>[] listElements = new List[]{new ArrayList<HtmlElement>(), new ArrayList<HtmlElement>()};

            for (int pageNum = 1; pageNum <= countPage; pageNum++) {
                if (pageNum > 1) {
                    try {
                        page = webClient.getPage(page.getUrl() + "&p=" + pageNum);
                    } catch (IOException e) {
                        log.warn("[Crawler] ***Citilink***: Page list products is not loaded " + e.getMessage());
                        return listItem;
                    }
                    webClient.waitForBackgroundJavaScript(waitTime);
                }

                try {
                    listElements[0] = page.getByXPath("//a[@class=' ProductCardVertical__name  Link js--Link Link_type_default']");
                    listElements[1] = page.getByXPath("//span[@class='ProductCardVerticalPrice__price-current_current-price js--ProductCardVerticalPrice__price-current_current-price ']");
                } catch (Exception e) {
                    log.warn("[Crawler] ***Citilink***: Searched tags name or price in Xpath not find " + e.getMessage());
                    return listItem;
                }

                for (int i = 0; i < listElements[0].size(); i++) {
                    //i+i это номер элемента цены товара, так как на сайте присутсвует по два элемента цены товара
                    //Ограничение, чтобы не попадали товары, которые не присутствуют на складе
                    //какая-то хунта todo: разобраться с i+i
                    if (i + i < listElements[1].size()) {
                        listItem.add(Item.builder()
                                .runId(run.getRunId())
                                .itemName(listElements[0].get(i).getTextContent())
                                .price(Double.parseDouble(listElements[1].get(i + i).getTextContent().trim().replaceAll("[^0-9]", "").replaceAll(" ", "")))
                                .itemName(productName)
                                .link(page.getUrl().toString()) //Сейчас ссылка общая по типу "https://www.citilink.ru/search/?text=iphone12&p=1". todo: сделать конкретную ссылку на Item по типу "https://www.citilink.ru/product/smartfon-apple-iphone-12-mgja3ru-a-chernyi-1428565/"
                                .build());
                    }
                }
            }

            return listItem;
        }

    }

    @Override
    public String getShop() {
        return "Citilink";
    }
}
