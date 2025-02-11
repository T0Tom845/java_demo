package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record TransactionDto(@JsonProperty("amount")
                             Double amount,
                             @JsonProperty("client_id")
                             Long clientId,
                             @JsonProperty("transaction_time")
                             @JsonFormat(pattern = "dd/MM/yyyy")
                             Date transactionTime,
                             @JsonProperty("status")
                             String status,
                             @JsonProperty("account_id")
                             Long accountId) {}
