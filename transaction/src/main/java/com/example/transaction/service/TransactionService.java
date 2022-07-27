package com.example.transaction.service;

import com.example.transaction.model.Account;
import com.example.transaction.model.FundWithdrawRequest;
import com.example.transaction.model.TransactionResponse;
import com.example.transaction.model.LocalTransferRequest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TransactionService {
    ResponseEntity<List<TransactionResponse>> getTransaction(Integer pageNo, Integer pageSize);

    ResponseEntity<String> makeLocalTransfer(LocalTransferRequest localTransferRequest, HttpServletRequest request);

    ResponseEntity<String> fundAccount(FundWithdrawRequest fundWithdrawRequest, HttpServletRequest request);

    ResponseEntity<String> withdrawFromAccount(FundWithdrawRequest fundWithdrawRequest, HttpServletRequest request);
}
