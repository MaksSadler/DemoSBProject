package com.example.springinaction.demosbproject.util;

import com.example.springinaction.demosbproject.util.dto.ExchangeRatesXmlDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ExchangeRatesXmlUtil {
    private final String urlTemplate;
    private final DateTimeFormatter formatter;
    private final RestTemplate restTemplate;

    public ExchangeRatesXmlUtil(@Value("${client.xml.url-template}") String urlTemplate,
                                @Value("${client.xml.date-template}") String dateTemplate,
                                RestTemplate restTemplate) {
        this.urlTemplate = urlTemplate;
        this.formatter = DateTimeFormatter.ofPattern(dateTemplate);
        this.restTemplate = restTemplate;
    }

    public ExchangeRatesXmlDTO getExchangeRatesByDate(LocalDate date) {
        try {
            return restTemplate.getForObject(makeURLByDate(date), ExchangeRatesXmlDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Unable to get exchange rate data from the source.");
        }
    }

    private String makeURLByDate(LocalDate date) {
        return String.format(urlTemplate, date.format(formatter));
    }
}
