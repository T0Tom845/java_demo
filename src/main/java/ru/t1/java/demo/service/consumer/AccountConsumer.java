package ru.t1.java.demo.service.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.TransactionDto;

@Component
@Slf4j
public class AccountConsumer {

    @KafkaListener(topics = "t1_demo_accounts", groupId = "account-consumer", containerFactory = "accountKafkaListenerContainerFactory")
    public void listenAccountTopic(ConsumerRecord<String, TransactionDto> record) {
        log.info("Received account {}", record.toString());
    }

}
