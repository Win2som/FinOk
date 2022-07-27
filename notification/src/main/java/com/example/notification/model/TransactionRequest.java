package com.example.notification.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private String firstName;
    private String email;
    private String debitAccount;
    private String creditAccount;
    private String transactionType;
    private Double amount;
    private String narration;
    private String status;
    private LocalDateTime createdAt;
}
