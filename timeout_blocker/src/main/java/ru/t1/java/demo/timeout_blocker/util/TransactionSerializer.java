package ru.t1.java.demo.timeout_blocker.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import ru.t1.java.demo.timeout_blocker.dto.TransactionDto;

@Slf4j
public class TransactionSerializer implements Serializer<TransactionDto> {
    private final ObjectMapper objectMapper;

    TransactionSerializer() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Override
    public byte[] serialize(String topic, TransactionDto data) {
        log.info("Сериализум объект {} в JSON", data.toString());

        try {
            if (data == null) {
                log.warn("Попытка сериализовать null объект");
                return null;
            }
            String json = objectMapper.writeValueAsString(data);
            log.info("Сериализован объект в JSON: {}", json);
            return json.getBytes();
        } catch (Exception e) {
            log.error("Ошибка сериализации TransactionDto: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка сериализации TransactionDto", e);
        }
    }

}

