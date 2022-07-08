package com.example.account.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private String bankName;
    @NotEmpty
    @Size(min = 10, message = "Account should have at least 10 numbers")
    private String accountNumber;
    @NotEmpty
    @Size(min = 10, max = 11, message = "BVN number must not be less than 10")
    private String bvn;
    private double balance = 0.00;
    @Size(min = 4, max = 4, message = "Pin can not be more than 4")
    private String pin;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime modifiedAt;
}
