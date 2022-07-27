package com.example.account.repository;

import com.example.account.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "select * from roles where lower(title) = lower(:title);", nativeQuery = true)
    Optional<Role> findRole(String title);
}
