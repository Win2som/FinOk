package com.example.notification.service;

import com.example.notification.email.EmailSender;
import com.example.notification.model.AccountRequest;
import com.example.notification.model.TransactionRequest;
import com.example.notification.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;


@AllArgsConstructor
@Service
@Slf4j
public class NotificationService {

    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private RestTemplate restTemplate;


    public void sendVerificationEmail(AccountRequest account) {

        //call confirmationTokenService to generate token
        String token = confirmationTokenService.generateConfirmationToken(account.getId());

        String link = "localhost:8081/api/v1/notification/confirm/"+account.getId()+"?token="+ token;
        log.info("Sent link with token {}", link);
        //call emailSender to send email
        emailSender.send(account.getEmail(), buildVerificationEmail(account.getFirstName(), link), "Confirm your email");

    }



    private String buildVerificationEmail(String name, String link) {
        return
                "<p>Hi " + name + ",</p>" +
                        "<p > Thank you for registering. Please click on the below link to activate your account: </p>" +
                        "<blockquote style=\"color:blue; font-size:25px;\"><a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. " +
                        "<p>See you soon</p>" +
                "\n" +
                "</div></div>";
    }


    @Transactional
    public String confirmToken(String token, Long account_id) {
       String confirmationResult = confirmationTokenService.confirmToken(token, account_id);

       String url = "http://ACCOUNT-SERVICE/api/account/enable/" + account_id;
       restTemplate.put(url, account_id, Long.class);

       return confirmationResult;
    }


    public String sendTransactionEmail(TransactionRequest transaction) {
        String email = buildTransactionEmail(transaction.getFirstName(), transaction.getDebitAccount(), transaction.getCreditAccount(),
        transaction.getAmount(), transaction.getTransactionType(), transaction.getStatus(), transaction.getNarration(), transaction.getCreatedAt());
        emailSender.send(transaction.getEmail(), email, getSubject(transaction.getTransactionType()));

        return "sent";
    }


    private String buildTransactionEmail(String name, String debitAccount, String creditAccount, Double amount, String transactionType, String narration, String status, LocalDateTime date) {
        return
                "<p>Hi " + name + ",</p>" +
                "<p> The following transaction occurred in your account:</p>" +
                "<p>Debit account: "+ debitAccount + "</p>" + "<p> Credit account: "+ creditAccount+"</p>" +
                "<p> Amount: "+ amount + "</p>" + "<p> Transaction type: "+ transactionType + "</p>" +
                "<p>Status: "+ status + "</p>" + "<p>Narration: "+ narration + "</p>" + "<p>Date: "+ date +
                "</p> <p>Thank you for trusting us</p>";
    }

    private String getSubject(String subject){

        log.info(subject);

        if(subject.equals("FUNDING")){
            return "Credit Transaction";
        }
        if(subject.equals("WITHDRAWAL")){
            return "Debit Transaction";
        }
        else{
            return "Local transfer";
        }
    }
}
