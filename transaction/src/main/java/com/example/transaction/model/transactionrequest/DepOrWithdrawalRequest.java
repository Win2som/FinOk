package com.example.transaction.model.transactionrequest;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
public class DepOrWithdrawalRequest {
    @NotEmpty(message = "Bank is required")
    private String  accountBank;
    @NotEmpty(message = "account number is required")
    private String accountNumber;
    @NotEmpty(message = "amount is required")
    private Double amount;
    private String narration;
    @NotEmpty(message = "Pin is required")
    private String pin;
}
