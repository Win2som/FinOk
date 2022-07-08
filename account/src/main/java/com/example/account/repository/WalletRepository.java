package com.example.account.repository;

import com.example.account.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByAccountNumber(String accountNumber);
}
