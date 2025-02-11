package ru.t1.java.demo.configuration.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import static ru.t1.java.demo.configuration.kafka.KafkaConfiguration.producerConfig;

@Configuration
public class DataSourceErrorConfiguration {


    @Bean
    public ProducerFactory<String, String> dataSourceErrorLogProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, String> dataSourceErrorLogKafkaTemplate() {
        return new KafkaTemplate<>(dataSourceErrorLogProducerFactory());
    }
}
