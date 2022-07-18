package com.example.transaction.util;

import com.example.transaction.model.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@AllArgsConstructor
public class AccountService {

    private RestTemplate restTemplate;

    public Account readAccount(String param){

        String url = "http://ACCOUNT-SERVICE/api/v1/account/get/?accountNum=" + param;

        return restTemplate.getForObject(url, Account.class);
    }


    public void updateAccount(Account account){
        String url = "http://ACCOUNT-SERVICE/api/v1/account/update/" + account.getId();

        restTemplate.put(url, account,account.getId());
    }

    public void validateAccountBalanceAndPin(Account account, Double amount, String pin){
        if(amount > account.getWallet().getBalance()){
            throw new RuntimeException("insufficient");
        }
        if(!pin.equals(account.getWallet().getPin())){
            throw new RuntimeException("incorrect pin");
        }
    }

}
