package com.example.transaction.service.money_in;

import com.example.transaction.model.money_in.InitializeTransactionRequest;
import com.example.transaction.model.money_in.InitializeTransactionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class InitializeTransactionServiceImpl implements InitializeTransactionService {

    @Value("${paystack_secret_key}")
    private String key;
    RestTemplate restTemplate = new RestTemplate();

    @Override
    public InitializeTransactionResponse initializeTransaction(InitializeTransactionRequest
                     initializeTransactionRequest){

        String url = "https://api.paystack.co/transaction/initialize";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + key);
        HttpEntity<InitializeTransactionRequest> entity = new HttpEntity<>(
                initializeTransactionRequest, headers);
        ResponseEntity<InitializeTransactionResponse> response = restTemplate.postForEntity(url, entity,
                                InitializeTransactionResponse.class);

        return response.getBody();
    }


    public Double applyValue(BigDecimal amount){
        //call accountService and update wallet balance
        return 0.0;
    }
}
