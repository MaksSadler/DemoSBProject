package com.example.springinaction.demosbproject.service;

import com.example.springinaction.core.ExchangeRateEvent;
import com.example.springinaction.demosbproject.dataEnum.DataSource;

import java.time.LocalDate;

public interface ExchangeRateService {

    Double getExchangeRate(String currency, LocalDate date);

    DataSource getDataSource();

    String createExchangeRate(ExchangeRateEvent event);
}
