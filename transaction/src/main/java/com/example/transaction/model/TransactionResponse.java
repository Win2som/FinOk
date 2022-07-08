package com.example.transaction.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TransactionResponse {

    private String debitAccount;
    private String creditAccount;
    private Double amount;
    private String narration;
    private String status;
    private LocalDateTime createdAt;

}
