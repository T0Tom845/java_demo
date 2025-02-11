package ru.t1.java.demo.service.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.AcceptedTransactionMessage;
import ru.t1.java.demo.dto.TransactionDto;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionProducer {
    private final KafkaTemplate<String, TransactionDto> kafkaTemplate;
    private final KafkaTemplate<String, AcceptedTransactionMessage> acceptedKafkaTemplate;

    public void send(TransactionDto transaction) {
        String key = UUID.randomUUID().toString();
        kafkaTemplate.sendDefault(key, transaction).whenComplete((result, exception) -> {
            if (exception == null) {
                log.info("Сообщение успешно отправлено: {}", result.toString());

            }else
                log.error("Ошибка при отправке сообщения: {}", exception.getMessage());
        });
    }

    //Отправляет сообщение в топик t1_demo_transaction_accept с информацией
    // {clientId, accountId, transactionId, timestamp, transaction.amount, account.balance}
    public void sendTransactionAccept(AcceptedTransactionMessage transaction) {
        String key = UUID.randomUUID().toString();
        acceptedKafkaTemplate.send("t1_demo_transaction_accept", key, transaction);
    }
}