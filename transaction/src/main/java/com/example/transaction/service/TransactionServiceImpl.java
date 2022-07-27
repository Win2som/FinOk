package com.example.transaction.service;

import com.example.amqp.RabbitMQMessageProducer;
import com.example.transaction.entity.Transaction;
import com.example.transaction.enums.TransactionType;
import com.example.transaction.model.*;
import com.example.transaction.repository.TransactionRepository;
import com.example.transaction.security.JwtUtils;
import com.example.transaction.util.AccountService;
import com.example.transaction.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final Utility utility;
    private final JwtUtils jwtUtils;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    private final AccountService accountService;


    @Override
    public ResponseEntity<List<TransactionResponse>> getTransaction(Integer pageNo, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Page<Transaction> transactionPage = transactionRepository.findAll(pageable);

        List<Transaction> transactionList = transactionPage.getContent();

        return new ResponseEntity<>(transactionList.stream().map(utility::fromTransaction)
                .collect(Collectors.toList()), HttpStatus.OK);
    }


    @Transactional
    @Override
    public ResponseEntity<String> makeLocalTransfer(LocalTransferRequest localTransferRequest, HttpServletRequest request) {

        Long accountId = jwtUtils.getUserIdFromJwtToken(jwtUtils.getJWTFromRequest(request));

        //call account microservice to get both accounts
        String requestParam1 = localTransferRequest.getSenderAcctNo();
        Account account1 = accountService.readAccount(requestParam1, request);
        log.info("first call to account service {}", account1);

        if(accountId != account1.getId()){
            throw new RuntimeException("Please input your correct account number");
        }

        String requestParam2 = localTransferRequest.getRecipientAcctNo();
        Account account2 = accountService.readAccount(requestParam2, request);
        log.info("second call to account service {}", account2);

        validateAccount(account1, account2, localTransferRequest);

        try {
            account1.getWallet().setBalance(account1.getWallet().getBalance().subtract(localTransferRequest.getAmount()));
            account2.getWallet().setBalance(account2.getWallet().getBalance().add(localTransferRequest.getAmount()));

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        log.info("account1 balance {}", account1.getWallet().getBalance());
        log.info("account2 balance {}", account2.getWallet().getBalance());

        //call account endpoint to update the two accounts ***
        accountService.updateAccount(account1, request);
        accountService.updateAccount(account2, request);

        //save transaction
        Transaction transaction = utility.fromTransferRequest(localTransferRequest, account1);
        transaction.setTransactionType(TransactionType.TRANSFER);
        Transaction savedTransaction = transactionRepository.save(transaction);

        //call notification service to send mails to the two account holders
        TransactionMailRequest mailRequest1 = utility.fromTransactionToMailRequest(savedTransaction,account1);
        mailRequest1.setFirstName(account1.getFirstName());
        mailRequest1.setEmail(account1.getEmail());
        mailRequest1.setCurrentBalance(account1.getWallet().getBalance().subtract(localTransferRequest.getAmount()));

        TransactionMailRequest mailRequest2 = utility.fromTransactionToMailRequest(savedTransaction,account1);
        mailRequest2.setFirstName(account2.getFirstName());
        mailRequest2.setEmail(account2.getEmail());
        mailRequest2.setCurrentBalance(account2.getWallet().getBalance().add(localTransferRequest.getAmount()));


        rabbitMQMessageProducer.publish(mailRequest1, "internal.notification.routing-key", "internal.exchange");
        rabbitMQMessageProducer.publish(mailRequest2, "internal.notification.routing-key", "internal.exchange");

        return new ResponseEntity<>("transfer successful", HttpStatus.OK);
    }



    public void validateAccount(Account account1, Account account2, LocalTransferRequest localTransferRequest){

        if(account1 == null || account2 == null){
            throw new RuntimeException("invalid account number");
        }
        validate(account1, localTransferRequest.getAmount(), localTransferRequest.getPin());
    }


    @Transactional
    @Override
    public ResponseEntity<String> fundAccount(FundWithdrawRequest fundWithdrawRequest, HttpServletRequest request) {

        Account account;
        Transaction savedTransaction;
        try{
         account = accountService.readAccount(fundWithdrawRequest.getAccountNum(), request);
        assert account != null;

        if(!fundWithdrawRequest.getPin().equals(account.getWallet().getPin())){
            throw new RuntimeException("incorrect pin");
        }
        if(fundWithdrawRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("bad request");
        }
        account.getWallet().setBalance(account.getWallet().getBalance().add(fundWithdrawRequest.getAmount()));
        accountService.updateAccount(account, request);

        log.info("account {} successfully funded with amount: {}", account, fundWithdrawRequest.getAmount());
        //save transaction
            Transaction transaction = Transaction.builder()
                    .amount(fundWithdrawRequest.getAmount())
                    .creditAccountNumber(fundWithdrawRequest.getAccountNum())
                    .transactionType(TransactionType.FUNDING)
                    .status("successful")
                    .createdAt(LocalDateTime.now())
                    .narration("--")
                    .debitAccountNumber("--")
                    .build();

            savedTransaction = transactionRepository.save(transaction);

    }catch(RuntimeException e){
        throw new RuntimeException(e.getMessage());
    }
        //publish mail
        TransactionMailRequest mailRequest = utility.fromTransactionToMailRequest(savedTransaction, account);
        mailRequest.setFirstName(account.getFirstName());
        mailRequest.setEmail(account.getEmail());
        mailRequest.setCurrentBalance(account.getWallet().getBalance().add(fundWithdrawRequest.getAmount()));

        rabbitMQMessageProducer.publish(mailRequest, "internal.notification.routing-key", "internal.exchange");

        return new ResponseEntity<>("Account funded successfully", HttpStatus.OK);
    }


    @Transactional
    @Override
    public ResponseEntity<String> withdrawFromAccount(FundWithdrawRequest fundWithdrawRequest, HttpServletRequest request) {
        Long accountId = jwtUtils.getUserIdFromJwtToken(jwtUtils.getJWTFromRequest(request));

        Account account;
        Transaction savedTransaction;
        try {
            account = accountService.readAccount(fundWithdrawRequest.getAccountNum(), request);
        }catch(RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }

            assert account != null;
            if(accountId != account.getId()){
                throw new RuntimeException("Please input your correct account number");
            }

            validateAccount(account, fundWithdrawRequest);

        try{
            account.getWallet().setBalance(account.getWallet().getBalance().subtract(fundWithdrawRequest.getAmount()));

            accountService.updateAccount(account, request);

            log.info("account {} withdrawal successful, amount: {}", account, fundWithdrawRequest.getAmount());

            //save transaction
            Transaction transaction = Transaction.builder()
                    .amount(fundWithdrawRequest.getAmount())
                    .debitAccountNumber(fundWithdrawRequest.getAccountNum())
                    .transactionType(TransactionType.WITHDRAWAL)
                    .status("successful")
                    .createdAt(LocalDateTime.now())
                    .narration("--")
                    .creditAccountNumber("--")
                    .build();

           savedTransaction = transactionRepository.save(transaction);

        }catch(RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }

        //publish mail
        TransactionMailRequest mailRequest = utility.fromTransactionToMailRequest(savedTransaction, account);
        mailRequest.setFirstName(account.getFirstName());
        mailRequest.setEmail(account.getEmail());
        mailRequest.setCurrentBalance(account.getWallet().getBalance().subtract(fundWithdrawRequest.getAmount()));

        rabbitMQMessageProducer.publish(mailRequest, "internal.notification.routing-key", "internal.exchange");

        return new ResponseEntity<>("Withdrawal successful", HttpStatus.OK);
    }


    public void validateAccount(Account account,FundWithdrawRequest fundWithdrawRequest){
        validate(account, fundWithdrawRequest.getAmount(), fundWithdrawRequest.getPin());
    }


    private void validate(Account account, BigDecimal amount, String pin) {
        if(amount.compareTo(account.getWallet().getBalance()) > 0){
            throw new RuntimeException("insufficient balance");
        }
        if(!pin.equals(account.getWallet().getPin())){
            throw new RuntimeException("incorrect pin");
        }
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("bad request");
        }
    }
}
