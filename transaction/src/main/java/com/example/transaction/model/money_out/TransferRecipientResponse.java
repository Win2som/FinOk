package com.example.transaction.model.money_out;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransferRecipientResponse {

    private boolean status;
    private String message;
    private Data data;

    @Getter
    @Setter
    @NoArgsConstructor
    public class Data {
        private boolean active;
        private String createdAt;
        private String currency;
        private String domain;
        private String id;
        private String integration;
        private String name;
        private String recipient_code;
        private String type;
        private String updatedAt;
        private boolean is_deleted;
        private Details details;

        @Getter
        @Setter
        @NoArgsConstructor
        public class Details {
            private String authorization_code;
            private String account_number;
            private String account_name;
            private String bank_code;
            private String bank_name;

        }
    }
}
