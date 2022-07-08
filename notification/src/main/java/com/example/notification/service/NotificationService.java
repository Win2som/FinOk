package com.example.notification.service;

import com.example.notification.email.EmailSender;
import com.example.notification.model.Account;
import com.example.notification.model.Transaction;
import com.example.notification.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;


@AllArgsConstructor
@Service
public class NotificationService {

    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private RestTemplate restTemplate;

    public boolean verifyEmail(Account account) {

        //call confirmationTokenService to generate token
        String token = confirmationTokenService.generateConfirmationToken(account.getId());

        String link = "localhost:8081/api/v1/notification/confirm/account.getId()?token="+ token;
        //call emailSender to send email
        emailSender.send(account.getEmail(), buildEmail(account.getFirstName(), link), "Confirm your email");
        //return a response to the microservice that called, find a way to make it wait until there is a confirmation--15 mins
        return confirmationTokenService.checkConfirmationTokenStatus(token);

    }


    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                " <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "\n" +
                "<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "\n" +
                "</div></div>";
    }


    public String confirmToken(String token, Long account_id) {
       String confirmationResult = confirmationTokenService.confirmToken(token);
       String url = "http://ACCOUNT-SERVICE/api/v1/account/enable";
       restTemplate.postForObject(url, account_id, Long.class);
       return confirmationResult;
    }


    public String sendTransactionEmail(Transaction transaction) {
        String email = buildEmailTwo(transaction.getFirstName(), transaction.getDebitAccount(), transaction.getCreditAccount(),
        transaction.getAmount(), transaction.getStatus(), transaction.getNarration(), transaction.getCreatedAt());
        emailSender.send(transaction.getEmail(), email, "Debit transaction");

        return "sent";
    }


    private String buildEmailTwo(String name, String debitAccount, String creditAccount, Double amount, String narration, String status, LocalDateTime date) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p>" +
                "<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> The following debit transaction occurred in your account:</p>" +
                " <p>Debit account: "+ debitAccount + "\n"+"Credit account: "+ creditAccount + "\n" + "Amount: "+ amount + "\n" +
                "Status: "+ status + "\n" + "Narration: "+ narration + "\n" + "Date: "+ date +"</p> <p>Thank you for trusting us</p>" +
                "\n" +
                "</div></div>";
    }
}
