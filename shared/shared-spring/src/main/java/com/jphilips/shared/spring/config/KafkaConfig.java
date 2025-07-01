package com.jphilips.shared.spring.config;

import com.jphiilips.shared.domain.dto.kafka.AppEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaTemplate<String, AppEvent<?>> kafkaTemplate(
            ProducerFactory<String, AppEvent<?>> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
