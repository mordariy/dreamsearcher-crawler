package com.example.crawler.common;

import com.example.crawler.entity.Product;
import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.List;

public interface Parser
{
    public default WebClient initialiseWebClient()
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

    public List<Product> parsePages(String productName, int waitTime, int countPage);

    public default void closeWebClient(WebClient webClient)
    {
        webClient.getCurrentWindow().getJobManager().removeAllJobs();
        webClient.close();
        System.gc();
    }

}
