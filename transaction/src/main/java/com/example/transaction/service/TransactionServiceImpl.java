package com.example.transaction.service;

import com.example.amqp.RabbitMQMessageProducer;
import com.example.transaction.entity.Transaction;
import com.example.transaction.model.Account;
import com.example.transaction.model.TransactionMailRequest;
import com.example.transaction.model.TransactionResponse;
import com.example.transaction.model.transactionrequest.TransferRequest;
import com.example.transaction.repository.TransactionRepository;
import com.example.transaction.util.Utility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private TransactionRepository transactionRepository;
    private final Utility utility;
    private RestTemplate restTemplate;
    private RabbitMQMessageProducer rabbitMQMessageProducer;


    @Override
    public ResponseEntity<List<TransactionResponse>> getTransaction(Integer pageNo, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Page<Transaction> transactionPage = transactionRepository.findAll(pageable);

        List<Transaction> transactionList = transactionPage.getContent();

        return new ResponseEntity<>(transactionList.stream().map(utility::fromTransaction)
                .collect(Collectors.toList()), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<String> makeLocalTransfer(TransferRequest transferRequest, Long account_id) {

        //call the account microservice, get the account info of the logged in user
        String url1 = "http://ACCOUNT-SERVICE/api/v1/account/get/"+account_id;
        String requestParam = transferRequest.getRecipientAcctNo();
        String url2 =  "http://ACCOUNT-SERVICE/api/v1/account/get?accountNum="+requestParam;

        Account account1 = restTemplate.getForObject(url1, Account.class);
        log.info("first call to account service {}", account1);

        //call an endpoint in the account microservice to get the account entity of the recipient
        Account account2 = restTemplate.getForObject(url2, Account.class);
        log.info("second call to account service {}", account2);

        if(account2 == null){
            throw new RuntimeException("invalid acct no");
        }
        assert account1 != null;
        if(transferRequest.getAmount() > account1.getWallet().getBalance()){
            throw new RuntimeException("insufficient");
        }
        if(!transferRequest.getPin().equals(account1.getWallet().getPin())){
            throw new RuntimeException("incorrect pin");
        }
        if(transferRequest.getAmount() <= 0){
            throw new RuntimeException("bad request");
        }

        account1.getWallet().setBalance(account1.getWallet().getBalance() - transferRequest.getAmount());
        account2.getWallet().setBalance(account2.getWallet().getBalance() + transferRequest.getAmount());

        log.info("account1 balance {}", account1.getWallet().getBalance());
        log.info("account2 balance {}", account2.getWallet().getBalance());

        //call account endpoint to update the two accounts ***
        String putUrl1 = "http://ACCOUNT-SERVICE/api/v1/account/update/"+account_id;
        String putUrl2 = "http://ACCOUNT-SERVICE/api/v1/account/update/"+account2.getId();

        restTemplate.put(putUrl1, account1,account_id);
        restTemplate.put(putUrl2, account2, account2.getId());


        Transaction transaction = utility.fromTransferRequest(transferRequest, account1);
        Transaction savedTransaction = transactionRepository.save(transaction);

        //call notification service to send mails to the two account holders

        TransactionMailRequest mailRequest1 = utility.fromTransactionToMailRequest(savedTransaction,account1);
        mailRequest1.setFirstName(account1.getFirstName());
        mailRequest1.setEmail(account1.getEmail());
        mailRequest1.setCurrentBalance(account1.getWallet().getBalance() - transferRequest.getAmount());

        TransactionMailRequest mailRequest2 = utility.fromTransactionToMailRequest(savedTransaction,account1);
        mailRequest2.setFirstName(account2.getFirstName());
        mailRequest2.setEmail(account2.getEmail());
        mailRequest2.setCurrentBalance(account2.getWallet().getBalance() + transferRequest.getAmount());

//        String url = "http://NOTIFICATION-SERVICE/api/v1/notification/notify";
//        try {
//            restTemplate.postForObject(url, mailRequest1, String.class);
//            restTemplate.postForObject(url, mailRequest2, String.class);
//        }catch(Exception e){
//            e.printStackTrace();
//        }

        rabbitMQMessageProducer.publish(mailRequest1, "internal.notification.routing-key", "internal.exchange");
        rabbitMQMessageProducer.publish(mailRequest2, "internal.notification.routing-key", "internal.exchange");

        return new ResponseEntity<>("transfer successful", HttpStatus.OK);

    }


}
