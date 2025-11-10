package com.example.springinaction.demosbproject.controller;

import com.example.springinaction.demosbproject.dataEnum.DataSource;
import com.example.springinaction.demosbproject.service.ExchangeRateService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ExchangeRateRouterController {
    private final Map<DataSource, ExchangeRateService> services;

    public ExchangeRateRouterController(List<ExchangeRateService> services) {
        this.services = services.stream()
                .collect(Collectors.toMap(ExchangeRateService::getDataSource, Function.identity()));
    }

    public Double getExchangeRate(String currency, LocalDate date, DataSource dataSource) {
        if (!services.containsKey(dataSource)) {
            throw new RuntimeException("No such data source found.");
        }
        return services.get(dataSource).getExchangeRate(currency, date);
    }
}
