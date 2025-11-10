package com.example.springinaction.demosbproject.service;

import com.example.springinaction.demosbproject.repository.ExchangeRatesRepository;
import com.example.springinaction.demosbproject.repository.dto.ExchangeRatesDatabaseDTO;
import com.example.springinaction.demosbproject.dataEnum.DataSource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ExchangeRateDatabaseService implements ExchangeRateService {
    private final ExchangeRatesRepository repository;

    public ExchangeRateDatabaseService(ExchangeRatesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Double getExchangeRate(String currency, LocalDate date) {
        ExchangeRatesDatabaseDTO dto = repository.findByCharCodeAndDate(currency, date)
                .orElseThrow(() -> new RuntimeException("No such currency found."));
        return dto.getValue() / dto.getNominal();
    }

    @Override
    public DataSource getDataSource() {
        return DataSource.DATABASE;
    }
}
