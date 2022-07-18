package com.example.transaction.controller.money_out;

import com.example.transaction.model.money_out.*;
import com.example.transaction.service.money_out.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/transaction")
@AllArgsConstructor
public class TransferController {
//to verify that account details are correct
    private TransferService transferService;
    @GetMapping("/resolve")
    public ResolveAccountResponse resolveAccount(@RequestParam("account_no")String accountNumber, @RequestParam("bank_code")String bank_code) throws IOException {
        return transferService.resolveAccount(accountNumber, bank_code);
    }
//to get list of supported banks
    @GetMapping("/banks")
    public List<BankResponse> getBanks(@RequestParam("currency") String currency){
        return transferService.getBanks(currency);
    }

    //to create transfer recipient
    @PostMapping("/create_recipient")
    public TransferRecipientResponse createRecipient(@RequestBody TransferRecipientRequest transferRecipientRequest){

        return transferService.createRecipient(transferRecipientRequest);
    }

    @PostMapping("/initiate_transfer")
    public TransferResponse initiateTransfer(@RequestBody TransferRequest transferRequest){
        return transferService.initiateTransfer(transferRequest);
    }
}
