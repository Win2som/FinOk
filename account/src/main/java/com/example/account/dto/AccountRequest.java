package com.example.account.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AccountRequest {
    @NotBlank(message = "The field firstName must not be empty")
    @Size(min = 2, message = "firstName should have at least two characters")
    private String firstName;

    @NotBlank(message = "The field lastName must not be empty")
    @Size(min = 2, message = "lastName should have at least two characters")
    private String lastName;

    @NotBlank(message = "The field email must not be empty")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "The field password must not be empty")
    @Size(min = 6, message = "Password should have at least 6 characters")
    private String password;

    @NotBlank(message = "The field password must not be empty")
    @Size(min = 6, message = "Password should have at least 6 characters")
    private String matchingPassword;

    @NotBlank(message = "The field phoneNumber must not be empty")
    @Size(min = 10, message = "Phone number should have at least 10 numbers")
    private String phoneNumber;

    private String address;

    @NotBlank(message = "The field bvn must not be empty")
    @Size(min = 10, message = "BVN should have at least 10 numbers")
    private String bvn;

    @NotBlank(message = "The field pin must not be empty")
    @Size(min = 4,max = 4, message = "Pin should have 4 numbers")
    private String pin;
}
