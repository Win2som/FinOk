package com.example.transaction.model.money_out;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
public class BankResponse {

    private boolean status;
    private String message;
    private Data data;


    @Getter @Setter @NoArgsConstructor
    public class Data{

    private String name;
    private String slug;
    private String code;
    private String longcode;
    private String gateway;
    private String pay_with_bank;
    private boolean active;
    private boolean is_deleted;
    private String country;
    private String currency;
    private String type;
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    }
}




