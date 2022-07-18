package com.example.transaction.service.money_out;


import com.example.transaction.model.money_out.*;

import java.io.IOException;
import java.util.List;

public interface TransferService {
    ResolveAccountResponse resolveAccount(String accountNumber, String bank_code) throws IOException;

    List<BankResponse> getBanks(String currency);

    TransferRecipientResponse createRecipient(TransferRecipientRequest transferRecipientRequest);

    TransferResponse initiateTransfer(TransferRequest transferRequest);
}
