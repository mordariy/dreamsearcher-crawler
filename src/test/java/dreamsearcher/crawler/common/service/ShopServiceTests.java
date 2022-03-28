package dreamsearcher.crawler.common.service;

import dreamsearcher.crawler.CrawlerApplicationTests;
import dreamsearcher.crawler.util.AppUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ShopServiceTests extends CrawlerApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getShopsTests() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/v1/shops"))
                .andExpect(status().isOk())
                .andReturn();

        String expectedResult = AppUtility.getContentFromResourceFile("json/shops/getShops__response.json");
        String response = result.getResponse().getContentAsString();

        Assertions.assertEquals(expectedResult, response);
    }

}
