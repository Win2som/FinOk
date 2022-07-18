package com.example.transaction.model.money_in;


import com.example.transaction.enums.Channel;
import com.example.transaction.enums.PaystackBearer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InitializeTransactionRequest {

    private Integer amount;
    private String email;
    private String plan;
    private String reference;
    private String subaccount;
    private PaystackBearer bearer = PaystackBearer.ACCOUNT;
    private String callback_url;
    private Float quantity;
    private Integer invoice_limit;
    private Integer transaction_charge;
    private List<Channel> channel;



}
