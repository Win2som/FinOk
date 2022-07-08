package com.example.transaction.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String creditAccountNumber;
    private String debitAccountNumber;
    private Double amount;
    private String narration;
    private String status;
    private LocalDateTime createdAt;
}
