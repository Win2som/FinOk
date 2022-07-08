package com.example.transaction.util;

import com.example.transaction.entity.Transaction;
import com.example.transaction.model.TransactionResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Utility {

    public TransactionResponse from(Transaction transaction){

        return TransactionResponse.builder()
                .debitAccount(transaction.getDebitAccountNumber())
                .creditAccount(transaction.getCreditAccountNumber())
                .amount(transaction.getAmount())
                .narration(transaction.getNarration())
                .status(transaction.getStatus())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
