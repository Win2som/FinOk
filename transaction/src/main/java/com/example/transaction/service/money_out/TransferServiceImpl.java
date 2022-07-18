package com.example.transaction.service.money_out;


import com.example.transaction.model.money_out.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TransferServiceImpl implements TransferService{

    RestTemplate restTemplate = new RestTemplate();
    @Value("${paystack_secret_key}")
    private String key;

    @Override
    public ResolveAccountResponse resolveAccount(String accountNumber, String bank_code) throws IOException {
        ResolveAccountResponse response = null;


        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet("https://api.paystack.co/bank/resolve/?account_number=" + accountNumber + "&bank_code=" + bank_code);
            request.addHeader("Content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + key);

            HttpResponse httpResponse = client.execute(request);


            ObjectMapper mapper = new ObjectMapper();

            response = mapper.readValue(httpResponse.getEntity().getContent(), ResolveAccountResponse.class);

            if (response == null || !(response.isStatus()) || !response.getData().getAccount_number().equals(accountNumber)) {
                throw new Exception("Could not resolve account");
            }

            log.info("working");
            log.info(response.getData().getAccount_number());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public List<BankResponse> getBanks(String currency) {
        List<BankResponse> response = new ArrayList<>();


        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet("https://api.paystack.co/bank?currency=" + currency);
            request.addHeader("Content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + key);

            HttpResponse httpResponse = client.execute(request);


            ObjectMapper mapper = new ObjectMapper();

            response.add(mapper.readValue(httpResponse.getEntity().getContent(), BankResponse.class));

            log.info("working");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    @Override
    public TransferRecipientResponse createRecipient(TransferRecipientRequest transferRecipientRequest) {

            String url = "https://api.paystack.co/transferrecipient";
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + key);
            HttpEntity<TransferRecipientRequest> entity = new HttpEntity<>(
                    transferRecipientRequest, headers);
            ResponseEntity<TransferRecipientResponse> response = restTemplate.postForEntity(url, entity,
                    TransferRecipientResponse.class);

            return response.getBody();
    }


    @Override
    public TransferResponse initiateTransfer(TransferRequest transferRequest) {
        String url = "https://api.paystack.co/transfer";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + key);
        HttpEntity<TransferRequest> entity = new HttpEntity<>(
                transferRequest, headers);
        ResponseEntity<TransferResponse> response = restTemplate.postForEntity(url, entity,
                TransferResponse.class);

        return response.getBody();
    }

}


