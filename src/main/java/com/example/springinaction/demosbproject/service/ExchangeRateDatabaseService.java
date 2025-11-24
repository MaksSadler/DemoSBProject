package com.example.springinaction.demosbproject.service;

import com.example.springinaction.core.ExchangeRateEvent;
import com.example.springinaction.demosbproject.repository.ExchangeRatesRepository;
import com.example.springinaction.demosbproject.repository.dto.ExchangeRatesDatabaseDTO;
import com.example.springinaction.demosbproject.dataEnum.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Component
public class ExchangeRateDatabaseService implements ExchangeRateService {
    private final ExchangeRatesRepository repository;
    private final KafkaTemplate<String, ExchangeRateEvent> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public ExchangeRateDatabaseService(ExchangeRatesRepository repository, KafkaTemplate<String, ExchangeRateEvent> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
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

    @Override
    public String createExchangeRate(ExchangeRateEvent event) {
        ExchangeRateEvent exchangeRateEvent = new ExchangeRateEvent(event.getId(), event.getCurrencyId(), event.getNumCode(),
                event.getCharCode(),event.getNominal(),event.getName(),event.getValue(),event.getDate());

        CompletableFuture<SendResult<String, ExchangeRateEvent>> future = kafkaTemplate
                .send("exchange-rate-created-events-topic", exchangeRateEvent);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                LOGGER.error("Failed to send message: {}", exception.getMessage());
            } else {
                LOGGER.info("Message sent successfully: {}", result.getRecordMetadata());
            }
        });

        LOGGER.info("Return: {}", exchangeRateEvent.getId());

        return String.valueOf(exchangeRateEvent.getId());
    }

}
