package com.example.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    @NotBlank
    @Size(min = 2, message = "firstName should have at least two characters")
    private String firstName;

    @NotBlank
    @Size(min = 2, message = "lastName should have at least two characters")
    private String lastName;

    @NotBlank
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank
    @Size(min = 6, message = "Password should have at least 6 characters")
    private String password;
//    private LocalDate dob;
    @NotBlank
    @Size(min = 10, message = "Phone number should have at least 10 numbers")
    private String phoneNumber;

    private String address;
    private String accountNumber;
    @NotBlank
    @Size(min = 10, message = "BVN should have at least 10 numbers")
    private String bvn;

    @NotBlank
    @Size(min = 4,max = 4, message = "Pin should have 4 numbers")
    private String pin;
}
