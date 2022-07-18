package com.example.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class LocalTransferRequest {

    @NotBlank
    @Size(min = 10, message = "Account number should have at least 10 characters")
    private String senderAcctNo;
    @NotBlank
    @Size(min = 10, message = "Account number should have at least 10 characters")
    private String recipientAcctNo;

    @NotNull(message = "amount is required")
    private Double amount;

    private String narration;

    @NotBlank
    @Size(min = 4,max = 4, message = "Pin should have 4 numbers")
    private String pin;
}
