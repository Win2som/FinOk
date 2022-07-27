package com.example.transaction.util;

import com.example.transaction.model.Account;
import com.example.transaction.security.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;


@Component
@RequiredArgsConstructor
public class AccountService {

    private final RestTemplate restTemplate;
    private final JwtUtils utils;

    @Value("${read-account-url}")
    private String readUrl;
    
    @Value("${update-account-url}")
    private String putUrl;

    public Account readAccount(String param, HttpServletRequest request){
//        String url = "http://ACCOUNT-SERVICE/api/v1/account/get/?accountNum=" + param;

        String finalUrl = readUrl + param;

        HttpEntity<HttpHeaders> jwtEntity = new HttpEntity<>(getHeaders(request));

        ResponseEntity<Account> account = restTemplate.exchange(finalUrl, HttpMethod.GET, jwtEntity,
                Account.class);

        return (account.getStatusCode() == HttpStatus.OK) ? account.getBody() : null;
    }



    public void updateAccount(Account account, HttpServletRequest request){

        HttpEntity<Account> jwtEntity = new HttpEntity<>(account, getHeaders(request));

        String finalPutUrl = putUrl + account.getId();
        restTemplate.exchange(finalPutUrl, HttpMethod.PUT, jwtEntity, Void.class, account.getId());
    }
//    public void updateAccount(Account account){
////        String url = "http://ACCOUNT-SERVICE/api/v1/account/update/" + account.getId();
//
//        String finalPutUrl = putUrl + account.getId();
//        restTemplate.put(finalPutUrl, account,account.getId());
//    }

    private HttpHeaders getHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", "Bearer " + utils.getJWTFromRequest(request));
        return headers;
    }



}
