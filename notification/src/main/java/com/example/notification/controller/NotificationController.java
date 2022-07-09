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
    public void verifyEmail(@RequestBody Account account){
        notificationService.verifyEmail(account);
    }


    @GetMapping(path = "/confirm/{id}")
    public String confirm(@RequestParam("token")String token, @PathVariable("id") Long account_id) {
        return notificationService.confirmToken(token, account_id);
    }


    @PostMapping("/notify")
    public String sendTransactionEmail(@RequestBody Transaction transaction){
        return notificationService.sendTransactionEmail(transaction);
    }
}
