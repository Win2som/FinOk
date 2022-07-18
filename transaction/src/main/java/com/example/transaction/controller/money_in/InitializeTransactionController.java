package com.example.transaction.controller.money_in;


import com.example.transaction.model.money_in.InitializeTransactionRequest;
import com.example.transaction.model.money_in.InitializeTransactionResponse;
import com.example.transaction.service.money_in.InitializeTransactionService;
import com.example.transaction.service.money_in.VerifyTransaction;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class InitializeTransactionController {


        private InitializeTransactionService initializeTransactionService;
        private VerifyTransaction verifyTransaction;

        @RequestMapping(path = "/initializetransaction", method = RequestMethod.POST)
        public InitializeTransactionResponse initializeTransaction(@RequestBody InitializeTransactionRequest
                        initializeTransactionRequest) {

            return initializeTransactionService.initializeTransaction(
                    initializeTransactionRequest);
        }

        @GetMapping("/verify")
        public void VerifyTransaction(@RequestParam(name = "ref") String reference) throws Exception {
            verifyTransaction.verifyTransaction(reference);
        }
}
