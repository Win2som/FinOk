package com.example.transaction.service.money_in;

import com.example.transaction.model.money_in.VerifyTransactionResponse;

public interface VerifyTransaction {
    VerifyTransactionResponse verifyTransaction(String reference) throws Exception;
}
