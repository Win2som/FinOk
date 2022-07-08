package com.example.account.service;


import com.example.account.entity.Account;
import com.example.account.model.AccountRequest;
import com.example.account.model.AccountResponse;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AccountService {

    ResponseEntity<String> creatAccount(AccountRequest accountRequest);

    ResponseEntity<String> updateAccount(Map<String, Object> accountRequest, Long id);

    ResponseEntity<AccountResponse> viewAccountByAcctHolder(Long id);

    ResponseEntity<Account> getAccountWithId(Long id);

    ResponseEntity<String> deleteAccount(Long id);

    ResponseEntity<Account> getAccountWithAccountNum(String accountNum);

    ResponseEntity<String> accountUpdate(Account account, Long id);

    void enableAccount(Long account_id);
}
