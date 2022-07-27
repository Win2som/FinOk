package com.example.account.service;

import com.example.account.config.JwtUtil;
import com.example.account.dto.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @Override
    public String login(AuthRequest authRequest){

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail().toLowerCase(), authRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return  "Bearer " + jwtUtil.generateToken(authentication);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
