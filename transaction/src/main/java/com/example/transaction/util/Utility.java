package com.example.transaction.util;

import com.example.transaction.entity.Transaction;
import com.example.transaction.model.Account;
import com.example.transaction.model.TransactionMailRequest;
import com.example.transaction.model.TransactionResponse;
import com.example.transaction.model.transactionrequest.TransferRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Utility {

    public TransactionResponse fromTransaction(Transaction transaction){

        return TransactionResponse.builder()
                .debitAccount(transaction.getDebitAccountNumber())
                .creditAccount(transaction.getCreditAccountNumber())
                .amount(transaction.getAmount())
                .narration(transaction.getNarration())
                .status(transaction.getStatus())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Transaction fromTransferRequest(TransferRequest transferRequest, Account account){

        return Transaction.builder()
                .amount(transferRequest.getAmount())
                .debitAccountNumber(account.getWallet().getAccountNumber())
                .creditAccountNumber(transferRequest.getRecipientAcctNo())
                .narration(transferRequest.getNarration())
                .status("successful")
                .createdAt(LocalDateTime.now())
                .build();
    }

    public TransactionMailRequest fromTransactionToMailRequest(Transaction transaction, Account account){

        return TransactionMailRequest.builder()
                .debitAccount(transaction.getDebitAccountNumber())
                .creditAccount(transaction.getCreditAccountNumber())
                .amount(transaction.getAmount())
                .narration(transaction.getNarration())
                .status(transaction.getStatus())
                .build();
    }
}
