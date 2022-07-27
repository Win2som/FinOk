package com.example.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class LocalTransferRequest {

    @NotBlank(message = "senderAcctNo must be provided")
    @Size(min = 10, message = "Account number should have at least 10 characters")
    private String senderAcctNo;

    @NotBlank(message = "recipientAcctNo must be provided")
    @Size(min = 10, message = "Account number should have at least 10 characters")
    private String recipientAcctNo;

    @NotNull(message = "amount is required")
    private BigDecimal amount;

    private String narration;

    @NotBlank(message = "pin must be provided")
    @Size(min = 4,max = 4, message = "Pin should have 4 numbers")
    private String pin;
}
