package ru.t1.java.demo.timeout_blocker.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.timeout_blocker.dto.TransactionDto;
import ru.t1.java.demo.timeout_blocker.dto.TransactionResult;
import ru.t1.java.demo.timeout_blocker.util.TransactionSerde;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class KafkaStreamProcessor {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;
    private static final String INPUT_TOPIC = "t1_demo_transaction_accept";
    private static final String RESULT_TOPIC = "t1_demo_transaction_result";
    @Value("${account.block.time-window-ms}")
    private Duration TIME_WINDOW;

    @Value("${account.block.count}")
    private int MAX_TRANSACTIONS;

    @Autowired
    public KafkaStreamProcessor(StreamsBuilderFactoryBean myKStreamBuilderFactoryBean) {
        this.streamsBuilderFactoryBean = myKStreamBuilderFactoryBean;
    }

    @PostConstruct
    public void buildStreamTopology() {
        TransactionSerde transactionSerde = new TransactionSerde();
        try {
            StreamsBuilder builder = streamsBuilderFactoryBean.getObject();
            if (builder == null) {
                throw new IllegalStateException("StreamsBuilder is not initialized");
            }


            KStream<String, TransactionDto> stream = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), transactionSerde));

            KStream<String, TransactionDto> keyedStream = stream.selectKey((key, transaction) -> transaction.clientId() + "_" + transaction.accountId());

// Считаем транзакции в скользящем временном окне
            KTable<Windowed<String>, Long> transactionCounts = keyedStream
                    .groupByKey()
                    .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(TIME_WINDOW.getSeconds())))
                    .count(Materialized.as("transaction-count-store"));

// Блокируем транзакции, если их слишком много за указанный период
            KStream<Long, TransactionResult> blockedTransactions = transactionCounts
                    .toStream((windowedKey, count) -> windowedKey.key()) // Преобразуем KTable в KStream с корректным ключом
                    .filter((key, count) -> count >= MAX_TRANSACTIONS)
                    .map((key, count) -> KeyValue.pair(Long.valueOf(key.split("_")[1]), new TransactionResult(null, Long.valueOf(key.split("_")[1]), "BLOCKED")));

// Фильтруем отклоненные транзакции (если сумма больше баланса)
            KStream<Long, TransactionResult> rejectedTransactions = keyedStream
                    .filter((key, transaction) -> transaction.transactionAmount() > transaction.accountBalance())
                    .map((key, transaction) -> KeyValue.pair(transaction.accountId(),
                            new TransactionResult(transaction.transactionId(), transaction.accountId(), "REJECTED")));

// Одобряем оставшиеся транзакции
            KStream<Long, TransactionResult> acceptedTransactions = keyedStream
                    .filter((key, transaction) -> transaction.transactionAmount() <= transaction.accountBalance())
                    .map((key, transaction) -> KeyValue.pair(transaction.accountId(),
                            new TransactionResult(transaction.transactionId(), transaction.accountId(), "ACCEPTED")));

// Объединяем потоки
            KStream<Long, TransactionResult> finalStream = blockedTransactions
                    .merge(rejectedTransactions)
                    .merge(acceptedTransactions);

// Отправляем результат в Kafka-топик
            finalStream.to("t1_demo_transaction_result", Produced.with(Serdes.Long(), new JsonSerde<>(TransactionResult.class)));

            finalStream.foreach((key, value) -> log.info("Отправлено: {}", value));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
}