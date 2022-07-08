package com.example.account.controller;

import com.example.account.entity.Account;
import com.example.account.model.AccountRequest;
import com.example.account.model.AccountResponse;
import com.example.account.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/new")
    public ResponseEntity<String> createAccount(@RequestBody AccountRequest accountRequest){
        log.info("new account created {}", accountRequest);
        return accountService.creatAccount(accountRequest);
    }

    @PostMapping("/enable")
    public void enableAccount(@RequestBody Long account_id){
        accountService.enableAccount(account_id);
    }
//for transaction service
    @PutMapping("/update/{id}")
    public ResponseEntity<String> accountUpdate(@RequestBody Account account, @PathVariable("id")Long id){
        log.info("account updated {}", account);
        return accountService.accountUpdate(account, id);
    }


    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateAccountInfo(@RequestBody Map<String, Object> accountRequest, @PathVariable("id")Long id){
        log.info("account updated {}", accountRequest);
        return accountService.updateAccount(accountRequest, id);
    }


    @GetMapping("/view/{id}")
    public ResponseEntity<AccountResponse> viewAccountByAcctHolder(@PathVariable("id")Long id){
        return accountService.viewAccountByAcctHolder(id);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Account> getAccountWithId(@PathVariable("id")Long id){
        return accountService.getAccountWithId(id);
    }

    @GetMapping("/get")
    public ResponseEntity<Account> getAccountWithAccountNum(@RequestParam(name = "accountNum")String accountNum){
        return accountService.getAccountWithAccountNum(accountNum);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable("id")Long id){
        return accountService.deleteAccount(id);
    }

    //TODO: forgot password url
    //TODO:forgot pin
}
