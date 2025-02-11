package ru.t1.java.demo.timeout_blocker.util;

import org.apache.kafka.common.serialization.Serdes;
import ru.t1.java.demo.timeout_blocker.dto.TransactionDto;
import ru.t1.java.demo.timeout_blocker.dto.TransactionResult;

public class TransactionSerde extends Serdes.WrapperSerde<TransactionDto>{
    public TransactionSerde() {
        super(new TransactionSerializer(), new TransactionDeserializer());
    }
}
