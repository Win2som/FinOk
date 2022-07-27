package com.example.account.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String accountNumber;
    private String bvn;
}
