package com.example.account.controller;

import com.example.account.dto.UpdateRequest;
import com.example.account.entity.Account;
import com.example.account.dto.AccountResponse;
import com.example.account.exception.CustomException;
import com.example.account.exception.ResourceNotFoundException;
import com.example.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PutMapping("/enable/{account_id}")
    public void enableAccount(@PathVariable("account_id") Long id) throws ResourceNotFoundException {
        accountService.enableAccount(id);
    }


    @GetMapping("")
    public ResponseEntity<AccountResponse> viewAccountByAcctHolder(){
        return accountService.viewAccountByAcctHolder();
    }

    @PutMapping
    public ResponseEntity<String> updateAccount(@RequestBody UpdateRequest updateRequest){
        return accountService.updateAccount(updateRequest);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> accountBalanceUpdate(@RequestBody Account account, @PathVariable("id")Long id) throws CustomException {
        return accountService.accountBalanceUpdate(account, id);
    }


    @GetMapping("/get")
    public ResponseEntity<Account> getAccountWithAccountNumber(@RequestParam(name = "accountNum")String accountNum){
        return accountService.getAccountWithAccountNum(accountNum);
    }


    @DeleteMapping("")
    public ResponseEntity<String> deleteAccount(){
        return accountService.deleteAccount();
    }

    //TODO: forgot password url
    //TODO:forgot pin
}
