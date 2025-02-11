package ru.t1.java.demo.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import ru.t1.java.demo.dto.IncomingResultTransactionDto;
import ru.t1.java.demo.dto.TransactionDto;

import java.time.Instant;

@Slf4j
public class IncomingResultTransactionDeserializer implements Deserializer<IncomingResultTransactionDto> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public IncomingResultTransactionDto deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                log.warn("Получены пустые данные для десериализации");
                return null;
            }
            String json = new String(data);
            log.info("Получено сообщение из Kafka: {}", json);  // Логируем JSON перед разбором

            JsonNode node = objectMapper.readTree(json);

            Long accountId = node.has("accountId") ? node.get("accountId").asLong() : null;
            Long transactionId = node.has("transactionId") ? node.get("transactionId").asLong() : null;
            String status = node.has("status") ? node.get("status").asText() : null;
            IncomingResultTransactionDto result = new IncomingResultTransactionDto(transactionId, accountId, status);
            log.info("Десериализован объект: {}", result);
            return result;

        } catch (Exception e) {
            log.error("Ошибка десериализации: {}", e.getMessage(), e);
            return null;
        }
    }
}
