package com.example.transaction.model.money_out;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class TransferResponse {
    private boolean status;
    private String message;
    private Data data;

    @Getter @Setter @NoArgsConstructor
    public class Data{
        private String reference;
        private Long integration;
        private String domain;
        private Double amount;
        private String currency;
        private String source;
        private String reason;
        private Long recipient;
        private boolean status;
        private String transfer_code;
        private Long id;
        private String createdAt;
        private String updatedAt;
    }
}
