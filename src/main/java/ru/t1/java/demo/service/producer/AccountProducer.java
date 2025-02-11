package ru.t1.java.demo.service.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.AccountDto;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountProducer {
    private final KafkaTemplate<String, AccountDto> kafkaTemplate;

    public void send(AccountDto account) {
        String key = UUID.randomUUID().toString();
        CompletableFuture<SendResult<String, AccountDto>> future = kafkaTemplate.sendDefault(key, account);
        future.whenComplete((result, exception) -> {
            if (exception == null) {
                log.info("Сообщение успешно отправлено: {}", result.toString());

            }else
                log.error("Ошибка при отправке сообщения: {}", exception.getMessage());
        });
    }
}
