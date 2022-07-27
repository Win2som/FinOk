package com.example.account.controller;

import com.example.account.dto.AccountRequest;
import com.example.account.dto.AuthRequest;
import com.example.account.exception.AccountAlreadyExistException;
import com.example.account.exception.ResourceNotFoundException;
import com.example.account.service.AccountService;
import com.example.account.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AccountService accountService;

    @PostMapping("")
    public ResponseEntity<String> createAccount(@Valid @RequestBody AccountRequest accountRequest) throws ResourceNotFoundException, AccountAlreadyExistException {
        return accountService.createAccount(accountRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(authService.login(authRequest), HttpStatus.OK);
    }


}
