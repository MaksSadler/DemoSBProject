package com.example.springinaction.demosbproject;

import com.example.springinaction.demosbproject.repository.ExchangeRatesRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
public class DemoSbProjectApplicationTests {

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExchangeRatesRepository repository;

    @BeforeAll
    public static void setup() {
        stubFor(WireMock
                .get(urlMatching("/xml/30/01/2025"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)));


        stubFor(WireMock.get(urlMatching("/xml/31/01/2025"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)));
    }

    @Test
    public void happyPathXmlTest() throws Exception {
        mockMvc.perform(get("/exchangerate")
                        .param("currency", "AUD")
                        .param("date", "2025-01-30")
                        .param("dataSource", "xml"))
                .andExpect(content().string("47.6537"));
    }

    @Test
    @Sql("/files/database/data.sql")
    public void happyPathDatabaseTest() throws Exception {
        mockMvc.perform(get("/exchangerate")
                        .param("currency", "AUD")
                        .param("date", "2025-01-10")
                        .param("dataSource", "database"))
                .andExpect(content().string("47.6537"));
    }

    @Test
    @Sql("/files/database/data.sql")
    public void noCurrencyFoundInDatabaseTest() throws Exception {
        mockMvc.perform(get("/exchangerate")
                        .param("currency", "XXX")
                        .param("date", "2025-01-30")
                        .param("dataSource", "database"))
                .andExpect(content().string("No such currency found."));
    }

    @Test
    public void noXmlDtoTest() throws Exception {
        mockMvc.perform(get("/exchangerate")
                        .param("currency", "AUD")
                        .param("date", "2025-01-31")
                        .param("dataSource", "xml"))
                .andExpect(content().string("Unable to get exchange rate data from the source."));
    }

}
