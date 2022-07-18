package com.example.transaction.service.money_in;


import com.example.transaction.model.money_in.VerifyTransactionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@AllArgsConstructor
public class VerifyTransactionImpl implements VerifyTransaction{

    private InitializeTransactionService initializeTransactionService;
    @Value("${paystack_secret_key}")
    private String key;


    public VerifyTransactionResponse verifyTransaction(String reference) throws Exception {

        VerifyTransactionResponse paystackresponse;


        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet("https://api.paystack.co/transaction/verify/" + reference);
            request.addHeader("Content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + key);

            HttpResponse response = client.execute(request);

            ObjectMapper mapper = new ObjectMapper();

            paystackresponse = mapper.readValue(response.getEntity().getContent(),VerifyTransactionResponse.class);

            if (paystackresponse == null || paystackresponse.getStatus().equals("false")) {
                throw new Exception("An error occurred while  verifying payment");
            } else if (paystackresponse.getData().getStatus().equals("success")) {
                //PAYMENT IS SUCCESSFUL, APPLY VALUE TO THE TRANSACTION
                log.info("working");
                log.info(paystackresponse.getData().getAmount().toString());
               initializeTransactionService.applyValue(paystackresponse.getData().getAmount());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Internal server error");
        }

        return paystackresponse;

    }


}
