package dreamsearcher.crawler.common.service;

import dreamsearcher.crawler.CrawlerApplicationTests;
import dreamsearcher.crawler.util.AppUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RunServiceTests extends CrawlerApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getRunsIfProcessedTrueTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/runs")
                .param("isProcessed", "true"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String expectedResult = AppUtility.getContentFromResourceFile("json/runs/getRuns_ifProcessedTrue__response.json");
        String response = result.getResponse().getContentAsString();

        Assertions.assertEquals(expectedResult, response);
    }

    @Test
    public void getRunsIfProcessedFalseTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/runs")
                        .param("isProcessed", "false"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String expectedResult = AppUtility.getContentFromResourceFile("json/runs/getRuns_ifProcessedFalse__response.json");
        String response = result.getResponse().getContentAsString();

        Assertions.assertEquals(expectedResult, response);
    }

}
