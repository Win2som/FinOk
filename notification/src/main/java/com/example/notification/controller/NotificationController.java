package com.example.notification.controller;

import com.example.notification.model.AccountRequest;
import com.example.notification.model.TransactionRequest;
import com.example.notification.service.NotificationService;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;


    @PostMapping("/verify")
    public void verifyEmail(@RequestBody AccountRequest account){
        notificationService.sendVerificationEmail(account);
    }


    @GetMapping(path = "/confirm/{id}")
    public String confirm(@RequestParam("token")String token, @PathVariable("id") Long account_id) {
        return notificationService.confirmToken(token, account_id);
    }


    @PostMapping("/notify")
    public String sendTransactionEmail(@RequestBody TransactionRequest transaction){
        return notificationService.sendTransactionEmail(transaction);
    }
}
