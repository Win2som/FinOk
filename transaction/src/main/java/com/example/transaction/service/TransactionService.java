package com.example.transaction.service;

import com.example.transaction.model.TransactionResponse;
import com.example.transaction.model.transactionrequest.TransferRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TransactionService {
    ResponseEntity<List<TransactionResponse>> getTransaction(Integer pageNo, Integer pageSize);

    ResponseEntity<String> makeLocalTransfer(TransferRequest transferRequest, Long account_id);
}
