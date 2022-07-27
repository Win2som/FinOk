package com.example.transaction.controller;

import com.example.transaction.model.FundWithdrawRequest;
import com.example.transaction.model.TransactionResponse;
import com.example.transaction.model.LocalTransferRequest;
import com.example.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;


    @GetMapping("")
    public ResponseEntity<List<TransactionResponse>>getTransactions(@RequestParam(name="pageNo", defaultValue = "0", required = false)Integer pageNo,
                                                                    @RequestParam(name="pageSize", defaultValue = "5", required = false)Integer pageSize){
        return transactionService.getTransaction(pageNo, pageSize);
    }


    @PostMapping("/local_transfer")
    public ResponseEntity<String> localTransfer(@Valid @RequestBody LocalTransferRequest localTransferRequest, HttpServletRequest request){
        return transactionService.makeLocalTransfer(localTransferRequest, request);
    }


    @PostMapping("/fund")
    public ResponseEntity<String> fundAccount(@RequestBody FundWithdrawRequest fundWithdrawRequest, HttpServletRequest request){
        return transactionService.fundAccount(fundWithdrawRequest, request);
    }


    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody FundWithdrawRequest fundWithdrawRequest, HttpServletRequest request){
        return transactionService.withdrawFromAccount(fundWithdrawRequest, request);
    }
}
