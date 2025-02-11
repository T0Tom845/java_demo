package ru.t1.java.demo.timeout_blocker.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize(as = TransactionResult.class)
@JsonSerialize(as = TransactionResult.class)
public record TransactionResult(
        Long transactionId,
        Long accountId,
        String status) {}
