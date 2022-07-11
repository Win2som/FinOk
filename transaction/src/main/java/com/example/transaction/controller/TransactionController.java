package com.example.transaction.controller;

import com.example.transaction.model.TransactionResponse;
import com.example.transaction.model.transactionrequest.TransferRequest;
import com.example.transaction.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    //TODO: fundWalletFromBank, withdrawFundsToBank, TransferFromWalletToWallet, getTransactionHistory

//    public ResponseEntity<String> fundWalletFromBank(){
//
//    }
//
//    public ResponseEntity<String> withdrawFundsToBank(){
//
//    }

    //this url/service will call the viewAccount of account service to get the account number
    @PostMapping("/{account_id}")
    public ResponseEntity<String> localTransfer(@Valid @RequestBody TransferRequest transferRequest, @PathVariable("account_id") Long account_id){
        return transactionService.makeLocalTransfer(transferRequest, account_id);
    }


    @GetMapping()
    public ResponseEntity<List<TransactionResponse>>getTransactions(@RequestParam(name="pageNo", defaultValue = "0", required = false)Integer pageNo,
                                                                    @RequestParam(name="pageSize", defaultValue = "5", required = false)Integer pageSize){

        return transactionService.getTransaction(pageNo, pageSize);
    }
}
