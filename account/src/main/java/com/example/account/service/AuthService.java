package com.example.account.service;

import com.example.account.dto.AuthRequest;

public interface AuthService {
    String login(AuthRequest authRequest);
}
