package com.example.notification.controller;

import com.example.notification.model.Account;
import com.example.notification.model.Transaction;
import com.example.notification.service.NotificationService;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;


    @PostMapping("/verify")
    public boolean verifyEmail(@RequestBody Account account){
        return notificationService.verifyEmail(account);
    }


    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return notificationService.confirmToken(token);
    }


    @PostMapping("/notify")
    public String sendTransactionEmail(@RequestBody Transaction transaction){
        return notificationService.sendTransactionEmail(transaction);
    }
}
