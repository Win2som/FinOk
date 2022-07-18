package com.example.transaction.model.money_out;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class TransferRecipientRequest {

    private String type;
    private String name;
    private String account_number;
    private String bank_code;
    private String currency;
}
