package com.example.transaction.model.money_out;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResolveAccountResponse{
    private boolean status;
    private String message;
    private Data data;



    @Getter @Setter @AllArgsConstructor
    public class Data{
        private String account_number;
        private String account_name;
        private Long bank_id;
    }
}
