package com.example.account.repository;

import com.example.account.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepo;

    @Test
    void testOnRole(){
        Role role = new Role();
        role.setTitle("USER");
        var newRole = roleRepo.save(role);

        System.out.println(newRole);

    }

}