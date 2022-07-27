package com.example.account.dto;

import lombok.*;


@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountPayload {
    private String firstName;
    private String email;
    private Long id;
}
