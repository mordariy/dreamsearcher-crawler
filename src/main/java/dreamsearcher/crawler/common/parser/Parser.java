package dreamsearcher.crawler.common.parser;

import com.gargoylesoftware.htmlunit.*;
import dreamsearcher.crawler.common.entity.Item;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.List;

public interface Parser {
    default WebClient initialiseWebClient() {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        WebClientOptions options = webClient.getOptions();
        options.setCssEnabled(false);
        options.setJavaScriptEnabled(false);
        options.setThrowExceptionOnFailingStatusCode(false);
        options.setThrowExceptionOnScriptError(false);
        options.setUseInsecureSSL(true);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.setAjaxController(new AjaxController() {
            @Override
            public boolean processSynchron(HtmlPage page, WebRequest request, boolean async) {
                return true;
            }
        });
        return webClient;
    }

    List<Item> parsePages(String productName, int waitTime, int countPage);

    String getShop();

}
