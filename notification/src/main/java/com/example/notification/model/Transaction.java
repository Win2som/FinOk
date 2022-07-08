package com.example.notification.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String firstName;
    private String email;
    private String debitAccount;
    private String creditAccount;
    private Double amount;
    private String narration;
    private String status;
    private LocalDateTime createdAt;
}
