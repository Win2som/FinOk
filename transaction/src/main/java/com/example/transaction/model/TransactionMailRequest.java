package com.example.transaction.model;

import com.example.transaction.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
    private BigDecimal amount;
    private String transactionType;
    private BigDecimal currentBalance;
    private String narration;
    private String status;
    private LocalDateTime createdAt;
}
