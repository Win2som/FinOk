package com.example.account.service;


import com.example.account.dto.UpdateRequest;
import com.example.account.entity.Account;
import com.example.account.dto.AccountRequest;
import com.example.account.dto.AccountResponse;
import com.example.account.exception.AccountAlreadyExistException;
import com.example.account.exception.CustomException;
import com.example.account.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

public interface AccountService {

    ResponseEntity<String> createAccount(AccountRequest accountRequest) throws ResourceNotFoundException, AccountAlreadyExistException;

    ResponseEntity<AccountResponse> viewAccountByAcctHolder();

//    ResponseEntity<String> deleteAccount(Long id);
    ResponseEntity<String> deleteAccount();

    ResponseEntity<Account> getAccountWithAccountNum(String accountNum);

    ResponseEntity<String> accountBalanceUpdate(Account user, Long id) throws CustomException;

    void enableAccount(Long account_id) throws ResourceNotFoundException;

    ResponseEntity<String> updateAccount(UpdateRequest updateRequest);
}
