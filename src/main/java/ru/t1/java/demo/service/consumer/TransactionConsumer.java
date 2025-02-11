package ru.t1.java.demo.service.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.service.TransactionService;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionConsumer {

    private final TransactionService transactionService;

    @KafkaListener(topics = "t1_demo_transactions", id = "transaction-consumer",
            groupId = "transaction-consumer", containerFactory = "transactionKafkaListenerContainerFactory")
    public void listenTransactionTopic(ConsumerRecord<String, TransactionDto> record) {
        log.info("Received transaction {}", record.value().toString());
        transactionService.processTransaction(record.value());
    }
}
