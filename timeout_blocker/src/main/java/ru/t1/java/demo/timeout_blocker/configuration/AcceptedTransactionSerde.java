package ru.t1.java.demo.timeout_blocker.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.Serdes;
import ru.t1.java.demo.timeout_blocker.dto.TransactionDto;

public class AcceptedTransactionSerde extends Serdes.WrapperSerde<TransactionDto> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public AcceptedTransactionSerde() {
        super(new Serializer<TransactionDto>() {
            @Override
            public byte[] serialize(String topic, TransactionDto data) {
                try {
                    return objectMapper.writeValueAsBytes(data);
                } catch (Exception e) {
                    throw new RuntimeException("Error serializing AcceptedTransactionDto", e);
                }
            }
        }, new Deserializer<TransactionDto>() {
            @Override
            public TransactionDto deserialize(String topic, byte[] data) {
                try {
                    return objectMapper.readValue(data, TransactionDto.class);
                } catch (Exception e) {
                    throw new RuntimeException("Error deserializing AcceptedTransactionDto", e);
                }
            }
        });
    }

    public static AcceptedTransactionSerde acceptedTransactionSerde() {
        return new AcceptedTransactionSerde();
    }
}

