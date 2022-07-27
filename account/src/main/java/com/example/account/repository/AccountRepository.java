package com.example.account.repository;

import com.example.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "select count(*)>0 from account where lower(email) = lower(:email)", nativeQuery = true)
    boolean existsByEmail(String email);

    @Query(value = "select * from account where wallet_id = :id", nativeQuery = true)
    Account findByWalletId(Long id);

    @Query(value = "select * from account where lower(email) = lower(:email)", nativeQuery = true)
    Account findByEmail(String email);
}
