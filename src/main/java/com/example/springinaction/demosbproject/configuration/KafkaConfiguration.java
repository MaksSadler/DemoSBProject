package com.example.springinaction.demosbproject.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Bean
    NewTopic createTopic() {
        return TopicBuilder.name("exchange-rate-created-events-topic")
                .partitions(3)
                .replicas(1)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }
}
