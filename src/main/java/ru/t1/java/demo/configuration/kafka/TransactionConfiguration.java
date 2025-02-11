package ru.t1.java.demo.configuration.kafka;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.t1.java.demo.dto.IncomingResultTransactionDto;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.util.IncomingResultTransactionDeserializer;

@Configuration
public class TransactionConfiguration {

    @Bean
    public ProducerFactory<String, TransactionDto> transactionProducerFactory() {
        return new DefaultKafkaProducerFactory<>(KafkaConfiguration.producerConfig());
    }

    @Bean
    public KafkaTemplate<String, TransactionDto> transactionKafkaTemplate() {
        KafkaTemplate<String, TransactionDto> kafkaTemplate = new KafkaTemplate<>(transactionProducerFactory());
        kafkaTemplate.setDefaultTopic("t1_demo_transactions");
        return kafkaTemplate;
    }

    @Bean
    ConsumerFactory<String, TransactionDto> transactionConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(KafkaConfiguration.consumerConfig(),
                new StringDeserializer(),
                new JsonDeserializer<>(TransactionDto.class));
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, TransactionDto>> transactionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransactionDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transactionConsumerFactory());
        return factory;
    }
    @Bean
    ConsumerFactory<String, IncomingResultTransactionDto> transactionResultConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(KafkaConfiguration.consumerConfig(),
                new StringDeserializer(),
                new IncomingResultTransactionDeserializer());
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, IncomingResultTransactionDto>> transactionResultKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, IncomingResultTransactionDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transactionResultConsumerFactory());
        return factory;
    }

}
