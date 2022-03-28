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

public class ItemServiceTests extends CrawlerApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getItemsByRunIdTests() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/v1/items")
                    .param("runId", "RUN-UUID-1111"))
                .andExpect(status().isOk())
                .andReturn();

        String expectedResult = AppUtility.getContentFromResourceFile("json/items/getItems__response.json");
        String response = result.getResponse().getContentAsString();

        Assertions.assertEquals(expectedResult, response);
    }
}
