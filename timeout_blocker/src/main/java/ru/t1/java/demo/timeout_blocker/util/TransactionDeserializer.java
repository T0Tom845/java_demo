package ru.t1.java.demo.timeout_blocker.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import ru.t1.java.demo.timeout_blocker.dto.TransactionDto;

import java.time.Instant;

@Slf4j
public class TransactionDeserializer implements Deserializer<TransactionDto> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public TransactionDto deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                log.warn("Получены пустые данные для десериализации");
                return null;
            }
            String json = new String(data);
            log.info("Получено сообщение из Kafka: {}", json);  // Логируем JSON перед разбором

            JsonNode node = objectMapper.readTree(json);

            Long clientId = node.has("clientId") ? node.get("clientId").asLong() : null;
            Long accountId = node.has("accountId") ? node.get("accountId").asLong() : null;
            Long transactionId = node.has("transactionId") ? node.get("transactionId").asLong() : null;
            Instant timestamp = null;
            if (node.has("timestamp")) {
                JsonNode timestampNode = node.get("timestamp");
                if (timestampNode.isNumber()) {
                    long epochValue = timestampNode.asLong();
                    if (epochValue > 10000000000L) {  // Если больше 10^10, то миллисекунды
                        timestamp = Instant.ofEpochMilli(epochValue);
                    } else {  // Если меньше, то секунды
                        timestamp = Instant.ofEpochSecond(epochValue);
                    }
                } else {
                    timestamp = Instant.parse(timestampNode.asText());
                }
            }
            Double transactionAmount = node.has("transactionAmount") ? node.get("transactionAmount").asDouble() : null;
            Double accountBalance = node.has("accountBalance") ? node.get("accountBalance").asDouble() : null;

            TransactionDto result = new TransactionDto(clientId, accountId, transactionId, timestamp, transactionAmount, accountBalance);
            log.info("Десериализован объект: {}", result);
            return result;

        } catch (Exception e) {
            log.error("Ошибка десериализации: {}", e.getMessage(), e);
            return null;
        }
    }
}
