package com.alten.back.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private static final String TOKEN_PREFIX = "Bearer ";
    private final AuthenticationManager authenticationManager;
    private final JwtHandler jwtHandler;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtHandler jwtHandler) {
        this.authenticationManager = authenticationManager;
        this.jwtHandler = jwtHandler;
    }

    public String authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return TOKEN_PREFIX + jwtHandler.generateToken(userDetails.getUsername());
    }
}

