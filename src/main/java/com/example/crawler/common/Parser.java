package com.example.crawler.common;

import com.example.crawler.entity.Product;
import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.List;

public interface Parser
{
    public static WebClient initialiseWebClient()
    {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.setAjaxController(new AjaxController(){
            @Override
            public boolean processSynchron(HtmlPage page, WebRequest request, boolean async)
            { return true; }});
        return webClient;
    }

    public HtmlPage getPageProductsListStore(String productName, int waitTime) throws IOException;

    public List<Product> parsePages(HtmlPage pageProductsListStore, int countPage);

}
