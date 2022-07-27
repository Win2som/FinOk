package com.example.account.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
}
