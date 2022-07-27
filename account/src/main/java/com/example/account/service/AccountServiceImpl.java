package com.example.account.service;


import com.example.account.dto.AccountPayload;
import com.example.account.dto.UpdateRequest;
import com.example.account.entity.Account;
import com.example.account.entity.Role;
import com.example.account.entity.Wallet;
import com.example.account.dto.AccountRequest;
import com.example.account.dto.AccountResponse;
import com.example.account.exception.AccountAlreadyExistException;
import com.example.account.exception.CustomException;
import com.example.account.exception.ResourceNotFoundException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.RoleRepository;
import com.example.account.repository.WalletRepository;
import com.example.amqp.RabbitMQMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
//    private final RestTemplate restTemplate;
    private final RoleRepository roleRepository;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    @Override
    @Transactional
    public ResponseEntity<String> createAccount(AccountRequest accountRequest) throws ResourceNotFoundException, AccountAlreadyExistException {

            if (accountRepository.existsByEmail(accountRequest.getEmail())) {
                throw new AccountAlreadyExistException("Account already exist");
            }

            Role role = roleRepository.findRole("USER")
                    .orElseThrow(() -> new ResourceNotFoundException("User role does not exist"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);

            Account account = Account.builder()
                    .firstName(accountRequest.getFirstName())
                    .lastName(accountRequest.getLastName())
                    .email(accountRequest.getEmail())
                    .password(passwordEncoder.encode(accountRequest.getPassword()))
                    .roles(roles)
                    .phoneNumber(accountRequest.getPhoneNumber())
                    .address(accountRequest.getAddress())
                    .wallet(buildWallet(accountRequest))
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .build();

        Account savedAccount;
        try {
             savedAccount = accountRepository.save(account);

        }catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        AccountPayload payload = AccountPayload.builder()
                .firstName(savedAccount.getFirstName())
                        .email(savedAccount.getEmail())
                                .id(savedAccount.getId())
                                        .build();

//
//        String url = "http://NOTIFICATION-SERVICE/api/v1/notification/verify";
//        restTemplate.postForObject(url, payload, void.class);

            rabbitMQMessageProducer.publish(payload, "internal.notification.routing-key",
                    "internal.exchange");


            return new ResponseEntity<>("Account created", HttpStatus.CREATED);

    }



    public Wallet buildWallet(AccountRequest accountRequest) {

        Wallet wallet = Wallet.builder()
                .accountNumber(getUniqueAccountNumber())
                .bvn(accountRequest.getBvn())
                .balance(BigDecimal.ZERO)
                .pin(accountRequest.getPin())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        log.info("Account number generated {}",wallet.getAccountNumber());
        return walletRepository.save(wallet);
    }


    //account number generator
    public String generateAccountNumber() {
        Random rn = new Random();
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            number.append(rn.nextInt(10));
        }
        return number.toString();
    }

    public String getUniqueAccountNumber(){
        String accountNumber = generateAccountNumber();

        Wallet wallet = walletRepository.findByAccountNumber(accountNumber);
        if (wallet == null){
            return accountNumber;
        }else{
            accountNumber = generateAccountNumber();
        }
        log.info("Account number generated {}", accountNumber);
        return accountNumber;
    }



    @Override
    public void enableAccount(Long account_id) throws ResourceNotFoundException {
        Account account = accountRepository.findById(account_id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("account with id %s not found",account_id)));
        account.setEnabled(true);
        accountRepository.save(account);
    }



    @Override
    public ResponseEntity<AccountResponse> viewAccountByAcctHolder() {

        Account currentUser = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new ResponseEntity<>(mapToAccountResponse(currentUser), HttpStatus.OK);
    }

    private AccountResponse mapToAccountResponse(Account account){
        return AccountResponse.builder()
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
                .phoneNumber(account.getPhoneNumber())
                .address(account.getAddress())
                .accountNumber(account.getWallet().getAccountNumber())
                .bvn(account.getWallet().getBvn())
                .build();
    }



    @Override
    public ResponseEntity<Account> getAccountWithAccountNum(String accountNum) {

        log.info(accountNum);

        Optional<Wallet> wallet = Optional.ofNullable(walletRepository.findByAccountNumber(accountNum));
        Account account = null;
        if (wallet.isPresent()) {

            account = accountRepository.findByWalletId(wallet.get().getId());
        }

        return new ResponseEntity<>(account,HttpStatus.OK);
    }


    @Override
    public ResponseEntity<String> updateAccount(UpdateRequest updateRequest) {
        Account currentUser = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(Objects.nonNull(updateRequest.getFirstName()) && !"".equalsIgnoreCase(updateRequest.getFirstName())){
            currentUser.setFirstName(updateRequest.getFirstName());
        }
        if(Objects.nonNull(updateRequest.getLastName()) && !"".equalsIgnoreCase(updateRequest.getLastName())){
            currentUser.setLastName(updateRequest.getLastName());
        }
        if(Objects.nonNull(updateRequest.getAddress()) && !"".equalsIgnoreCase(updateRequest.getAddress())){
            currentUser.setAddress(updateRequest.getAddress());
        }
        if(Objects.nonNull(updateRequest.getPhoneNumber()) && !"".equalsIgnoreCase(updateRequest.getPhoneNumber())){
            currentUser.setPhoneNumber(updateRequest.getPhoneNumber());
        }

        accountRepository.save(currentUser);
        return new ResponseEntity<>("Update Successful", HttpStatus.OK);
    }


    @Override
    public ResponseEntity<String> accountBalanceUpdate(Account account, Long id) throws CustomException {

        log.info(String.valueOf(account.getWallet().getBalance()));

        int updatedCount = walletRepository.updateAccountBalance(account.getWallet().getId(), account.getWallet().getVersion(), account.getWallet().getBalance());
        if (updatedCount < 1) throw new CustomException("Please try again !!!");

        return new ResponseEntity<>("account updated by transaction",HttpStatus.OK);
    }

//    @Override
//    @Transactional
//    public ResponseEntity<String> accountUpdate(Account account, Long id) throws CustomException {
//        log.info("in account update");
//        log.info("in account update");
//        log.info("in account update");
//        Account acct = accountRepository.findById(id).
//                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("account with id %s not found",id)));
//        BeanUtils.copyProperties(account, acct);
//        log.info(String.valueOf(account.getWallet().getBalance()));
//
//        int updatedCount = walletRepository.updateAccountBalance(account.getWallet().getId(), account.getWallet().getVersion(), account.getWallet().getBalance());
//        if (updatedCount < 1) throw new CustomException("Please try again !!!");
////        walletRepository.save(account.getWallet());
//
//        accountRepository.save(acct);
//
//        return new ResponseEntity<>("account updated by transaction",HttpStatus.OK);
//    }
    @Override
    public ResponseEntity<String> deleteAccount() {
        Account currentUser = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountRepository.delete(currentUser);

        return new ResponseEntity<>("Account deleted", HttpStatus.OK);
    }

}
