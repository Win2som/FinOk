package com.example.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TransactionMailRequest {
    private String firstName;
    private String email;
    private String debitAccount;
    private String creditAccount;
    private Double amount;
    private Double currentBalance;
    private String narration;
    private String status;
    private LocalDateTime createdAt;
}
