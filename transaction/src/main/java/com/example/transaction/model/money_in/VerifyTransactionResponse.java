package com.example.transaction.model.money_in;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter @Setter
public class VerifyTransactionResponse {

    private String status;

    private String message;

    private Data data;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter @Setter
    public  class Data {
        private BigDecimal amount;
        private String currency;
        private String transaction_date;
        private String status;
        private String reference;
        private String domain;
        private String gateway_response;
        private String message;
        private String channel;
        private String ip_address;
        private String fees;
        private String plan;
        private String paid_at;
//        private Authorization authorization;
    }

    }
