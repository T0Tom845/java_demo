package ru.t1.java.demo.timeout_blocker.configuration;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;

@Configuration
public class KafkaStreamsConfig {

    @Bean
    public KafkaStreamsConfiguration streamsConfiguration() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "time-count-blocker");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class.getName());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092,localhost:39092,localhost:49092");
        return new KafkaStreamsConfiguration(props);
    }
    @Bean
    public StreamsBuilderFactoryBean myKStreamBuilder(KafkaStreamsConfiguration streamsConfiguration) {
        return new StreamsBuilderFactoryBean(streamsConfiguration);
    }

//
//    @Bean
//    public KStream<Long, TransactionResult> kafkaStream(StreamsBuilder builder) {
//        KStream<Long, AcceptedTransactionDto> stream = builder.stream("t1_demo_transactions_accept");
//
//        KTable<Windowed<Long>, Long> transactionCounts = stream
//                .groupBy((key, transaction) -> transaction.accountId())
//                .windowedBy(TimeWindows.ofSizeWithNoGrace(T))
//                .count();
//
//        KStream<Long, TransactionResult> blockedTransactions = transactionCounts
//                .toStream()
//                .filter((windowedKey, count) -> count >= N)
//                .flatMap((windowedKey, count) -> {
//                    List<KeyValue<Long, TransactionResult>> results = new ArrayList<>();
//                    results.add(new KeyValue<>(windowedKey.key(), new TransactionResult(null, windowedKey.key(), "BLOCKED")));
//                    return results;
//                });
//
//        KStream<Long, TransactionResult> rejectedTransactions = stream
//                .filter((key, transaction) -> transaction.transactionAmount() > transaction.accountBalance())
//                .map((key, transaction) -> KeyValue.pair(transaction.accountId(), new TransactionResult(transaction.transactionId(), transaction.accountId(), "REJECTED")));
//
//        KStream<Long, TransactionResult> acceptedTransactions = stream
//                .filter((key, transaction) -> transaction.transactionAmount() <= transaction.accountBalance())
//                .map((key, transaction) -> KeyValue.pair(transaction.accountId(), new TransactionResult(transaction.transactionId(), transaction.accountId(), "ACCEPTED")));
//
//        KStream<Long, TransactionResult> finalStream = blockedTransactions
//                .merge(rejectedTransactions)
//                .merge(acceptedTransactions);
//
//        finalStream.to("t1_demo_transaction_result", Produced.with(Serdes.Long(), new JsonSerde<>(TransactionResult.class)));
//
//        return finalStream;
//    }
}
