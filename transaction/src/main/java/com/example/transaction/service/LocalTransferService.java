package com.example.transaction.service;

import com.example.transaction.model.TransactionResponse;
import com.example.transaction.model.LocalTransferRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LocalTransferService {
    ResponseEntity<List<TransactionResponse>> getTransaction(Integer pageNo, Integer pageSize);

    ResponseEntity<String> makeLocalTransfer(LocalTransferRequest localTransferRequest);
}
