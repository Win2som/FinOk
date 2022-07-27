package com.example.notification.rabbitmq;

import com.example.notification.model.AccountRequest;
import com.example.notification.model.TransactionRequest;
import com.example.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class NotificationConsumer {
    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void consumer(AccountRequest accountRequest){
        log.info("consumed {} from queue", accountRequest.toString());
        notificationService.sendVerificationEmail(accountRequest);

    }


    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void consumer(TransactionRequest transactionRequest){
        log.info("consumed {} from queue", transactionRequest.toString());
        notificationService.sendTransactionEmail(transactionRequest);

    }
}
