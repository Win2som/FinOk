package com.example.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
//    private LocalDate dob;
    private String phoneNumber;
    private String address;
    private String accountNumber;
    private String bvn;
    private String pin;
}
