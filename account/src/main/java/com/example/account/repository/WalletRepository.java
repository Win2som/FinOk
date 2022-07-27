package com.example.account.repository;

import com.example.account.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @Query(value = "select * from wallet where account_number = :accountNumber", nativeQuery = true)
    Wallet findByAccountNumber(String accountNumber);


    @Modifying
    @Transactional
    @Query(value = "update wallet set balance = :balance, version = version + 1 where id = :walletId and version = :version",
            nativeQuery = true)
    int updateAccountBalance(@Param("walletId") Long walletId, @Param("version") Long version,
                             @Param("balance") BigDecimal balance);
}
