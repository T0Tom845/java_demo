package ru.t1.java.demo.configuration.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.t1.java.demo.dto.AcceptedTransactionMessage;

@Configuration
public class AcceptedTransactionKafkaConfiguration {


    @Bean
    public ProducerFactory<String, AcceptedTransactionMessage> acceptedTransactionProducerKafkaFactory() {

        return new DefaultKafkaProducerFactory<>(KafkaConfiguration.producerConfig());
    }

    @Bean
    public KafkaTemplate<String, AcceptedTransactionMessage> acceptedKafkaTemplate() {
        return new KafkaTemplate<>(acceptedTransactionProducerKafkaFactory());
    }

}
