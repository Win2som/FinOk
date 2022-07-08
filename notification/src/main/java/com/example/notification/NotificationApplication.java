package com.example.notification;

import com.example.notification.email.EmailSender;
import com.example.notification.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class NotificationApplication {

    @Autowired
    private EmailSender emailSender;

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void sendMail(){
//        emailSender.send("obidikechisom40@gmail.com", "This is the mail you are waiting for");
//    }

//    @EventListener(ApplicationReadyEvent.class)
    @PostMapping("/entry")
    public void sendMail(@RequestBody Transaction transaction){
        emailSender.send(transaction.getEmail(), transaction.getNarration(), "testing");
    }

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
