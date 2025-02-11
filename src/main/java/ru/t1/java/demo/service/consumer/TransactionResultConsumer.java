package ru.t1.java.demo.service.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.IncomingResultTransactionDto;
import ru.t1.java.demo.service.TransactionService;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionResultConsumer {
    private final TransactionService transactionService;
    @KafkaListener(topics = "t1_demo_transaction_result", groupId = "transaction-result-consumer", containerFactory = "transactionResultKafkaListenerContainerFactory")
    public void handle(ConsumerRecord<String, IncomingResultTransactionDto> transactionDto) {
        log.info("Received transaction {}", transactionDto.value());
        transactionService.processTransactionResult(transactionDto.value());

    }
}
