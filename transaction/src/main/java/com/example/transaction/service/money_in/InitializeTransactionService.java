package com.example.transaction.service.money_in;


import com.example.transaction.model.money_in.InitializeTransactionRequest;
import com.example.transaction.model.money_in.InitializeTransactionResponse;

import java.math.BigDecimal;

public interface InitializeTransactionService {
    InitializeTransactionResponse initializeTransaction (InitializeTransactionRequest
                                                                 initializeTransactionRequestDTO );

    Double applyValue(BigDecimal amount);
}
