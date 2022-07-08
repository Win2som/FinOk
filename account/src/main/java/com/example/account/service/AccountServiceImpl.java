package com.example.account.service;
import com.example.account.entity.Account;
import com.example.account.entity.Wallet;
import com.example.account.model.AccountRequest;
import com.example.account.model.AccountResponse;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.WalletRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final WalletRepository walletRepository;
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> creatAccount(AccountRequest accountRequest) {

        Account account = Account.builder()
                .firstName(accountRequest.getFirstName())
                .lastName(accountRequest.getLastName())
                .email(accountRequest.getEmail())
                .password(accountRequest.getPassword())
//                .dob(accountRequest.getDob())
                .phoneNumber(accountRequest.getPhoneNumber())
                .address(accountRequest.getAddress())
                .wallet(buildWallet(accountRequest))
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        Account savedAccount = accountRepository.save(account);


        Account account1 = Account.builder()
                .email(savedAccount.getEmail())
                .firstName(savedAccount.getFirstName())
                .id(savedAccount.getId())
                .build();

        String url = "http://localhost:8081/api/v1/notification/verify";
        //should
        restTemplate.postForObject(url, account1,Boolean.class);

//        if(isEmailVerified){
//            savedAccount.setEnabled(true);
//            accountRepository.save(savedAccount);
//        }

        return new ResponseEntity<>("Account created", HttpStatus.CREATED);
    }

    public Wallet buildWallet(AccountRequest accountRequest) {

        Wallet wallet = Wallet.builder()
                .accountNumber(accountRequest.getAccountNumber())
                .bvn(accountRequest.getBvn())
                .pin(accountRequest.getPin())
                .build();
        return walletRepository.save(wallet);
    }


    @Override
    public void enableAccount(Long account_id) {
        Account account = accountRepository.findById(account_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("account with id %s not found",account_id)));
        account.setEnabled(true);
        accountRepository.save(account);
    }

    @Override
    public ResponseEntity<String> updateAccount(Map<String, Object> accountRequest, Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("account with id %s not found",id)));

        accountRequest.forEach((k,v) -> {
            Field field = ReflectionUtils.findField(Account.class, k);
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.setField(field, account, v);
        });

        accountRepository.save(account);

        return new ResponseEntity<>("Account updated", HttpStatus.OK);
    }


    @Override
    public ResponseEntity<AccountResponse> viewAccountByAcctHolder(Long id) {
        Account account = accountRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("account with id %s not found",id)));

        AccountResponse accountResponse = AccountResponse.builder()
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
//                .dob(account.getDob())
                .phoneNumber(account.getPhoneNumber())
                .address(account.getAddress())
                .accountNumber(account.getWallet().getAccountNumber())
                .bvn(account.getWallet().getBvn())
                .build();

        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Account> getAccountWithId(Long id) {
        Account account = accountRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("account with id %s not found",id)));

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Account> getAccountWithAccountNum(String accountNum) {
        Optional<Wallet> wallet = Optional.ofNullable(walletRepository.findByAccountNumber(accountNum));
        Account account = null;
        if (wallet.isPresent()) {
            account = accountRepository.findByWallet(wallet.get());
        }

        return new ResponseEntity<>(account,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> accountUpdate(Account account, Long id) {
        Account acct = accountRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("account with id %s not found",id)));
        BeanUtils.copyProperties(account, acct);
        log.info(String.valueOf(account.getWallet().getBalance()));

        walletRepository.save(account.getWallet());
        accountRepository.save(acct);

        return new ResponseEntity<>("account updated by transaction",HttpStatus.OK);
    }


    @Override
    public ResponseEntity<String> deleteAccount(Long id) {
        Account account = accountRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("account with id %s not found",id)));

        accountRepository.delete(account);

        return new ResponseEntity<>("Account deleted", HttpStatus.OK);
    }




}
