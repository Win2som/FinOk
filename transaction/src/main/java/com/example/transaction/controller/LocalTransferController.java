package com.example.transaction.controller;

import com.example.transaction.model.TransactionResponse;
import com.example.transaction.model.LocalTransferRequest;
import com.example.transaction.service.LocalTransferService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/transaction")
public class LocalTransferController {
    private final LocalTransferService localTransferService;

    @PostMapping("/local_transfer")
    public ResponseEntity<String> localTransfer(@Valid @RequestBody LocalTransferRequest localTransferRequest){
        return localTransferService.makeLocalTransfer(localTransferRequest);
    }


    @GetMapping()
    public ResponseEntity<List<TransactionResponse>>getTransactions(@RequestParam(name="pageNo", defaultValue = "0", required = false)Integer pageNo,
                                                                    @RequestParam(name="pageSize", defaultValue = "5", required = false)Integer pageSize){

        return localTransferService.getTransaction(pageNo, pageSize);
    }
}
