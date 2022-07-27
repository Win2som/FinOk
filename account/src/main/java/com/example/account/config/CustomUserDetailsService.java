package com.example.account.config;

import com.example.account.entity.Account;
import com.example.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    private Collection<? extends GrantedAuthority> authorities;

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Account account = accountRepository.findByEmail(email);
//        return new org.springframework.security.core.userdetails.User(account.getEmail(), account.getPassword(), new ArrayList<>());
//    }


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) {
        try {
            //find app user by email
            Optional<Account> account = Optional.ofNullable(accountRepository.findByEmail(email));
            //if the app user is present and is active, add the app user roles to the granted authorities and return app user
            if (account.isPresent()) {
                Account accountObj = account.get();
                if (accountObj.isEnabled()) {
                    authorities = accountObj.getAuthorities();
                    return accountObj;
                }
                throw new UsernameNotFoundException("Account is disabled, please verify your email");
            }
            throw new UsernameNotFoundException("invalid email or password");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
