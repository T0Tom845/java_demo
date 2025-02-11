package ru.t1.java.demo.configuration.kafka;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.t1.java.demo.dto.AccountDto;


@Configuration
public class AccountKafkaConfiguration {



    @Bean
    public ProducerFactory<String, AccountDto> accountProducerFactory() {
        return new DefaultKafkaProducerFactory<>(KafkaConfiguration.producerConfig());
    }

    @Bean
    public KafkaTemplate<String, AccountDto> accountKafkaTemplate() {
        KafkaTemplate<String, AccountDto> kafkaTemplate = new KafkaTemplate<>(accountProducerFactory());
        kafkaTemplate.setDefaultTopic("t1_demo_accounts");
        return kafkaTemplate;
    }




    @Bean
    ConsumerFactory<String, AccountDto> accountConsumerFactory() {

        return new DefaultKafkaConsumerFactory<>(KafkaConfiguration.consumerConfig(),
                new StringDeserializer(),
                new JsonDeserializer<>(AccountDto.class));
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, AccountDto> accountKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AccountDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(accountConsumerFactory());
        return factory;
    }

}
