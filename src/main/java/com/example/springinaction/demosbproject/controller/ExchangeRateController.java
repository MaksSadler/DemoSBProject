package com.example.springinaction.demosbproject.controller;

import com.example.springinaction.demosbproject.dataEnum.DataSource;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Stream;

@Validated
@RestController
@Slf4j
public class ExchangeRateController {
    private final ExchangeRateRouterController router;

    public ExchangeRateController(ExchangeRateRouterController router) {
        this.router = router;
    }

    @GetMapping("/exchangerate")
    public Double getExchangeRate(@RequestParam(required = false) @NotNull String currency,
                                  @RequestParam(required = false) @PastOrPresent @NotNull LocalDate date,
                                  @RequestParam(required = false, defaultValue = "json") DataSource dataSource) {
        return router.getExchangeRate(currency, date, dataSource);
    }

    @Scheduled(cron = "0 00 8 * * *")
    public void getCurrenciesValue() throws IOException {
        String url = "https://www.cbr-xml-daily.ru/daily.xml";

        Document doc = Jsoup.connect(url)
                .userAgent("Yandex")
                .timeout(5000)
                .referrer("https://yandex.ru")
                .get();
        Stream<Document> currencies = Stream.of(doc).toList().stream();
        currencies.forEach(System.out::println);
    }

    @Scheduled(fixedDelay = 5000)
    public void getCurrenciesValueTest() {
        System.out.println("Обновил валюту.");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String exceptionHandler(Exception e) {
        return e.getMessage();
    }
}
