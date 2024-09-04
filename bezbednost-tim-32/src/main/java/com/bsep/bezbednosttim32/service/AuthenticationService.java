package com.bsep.bezbednosttim32.service;

import com.bsep.bezbednosttim32.auth.LoginResponse;
import com.bsep.bezbednosttim32.auth.LoginRequest;
import com.bsep.bezbednosttim32.model.User;
import com.bsep.bezbednosttim32.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jboss.aerogear.security.otp.Totp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        logger.info("Attempting to authenticate user with email: [{}]", request.getEmail());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception e) {
            logger.error("Authentication failed for user with email: [{}], Error: {}", request.getEmail(), e.getMessage());
            throw new RuntimeException("Authentication failed", e);
        }

        var user = repository.findByEmail(request.getEmail()).orElseThrow(() -> {
            logger.error("User not found during login with email: [{}]", request.getEmail());
            return new NoSuchElementException("User not found");
        });

        logger.info("User [{}] authenticated successfully, generating tokens.", user.getId());
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        logger.info("Tokens generated for user [{}].", user.getId());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }


    public LoginResponse refreshAccessToken(String refreshToken) {
        logger.info("Attempting to refresh access token for token: {}", refreshToken); // Careful with sensitive data, consider hashing if needed

        // First, validate the refresh token
        if (!jwtService.isTokenValid(refreshToken)) {
            logger.warn("Attempt to refresh using an invalid refresh token: {}", refreshToken);
            throw new SecurityException("Invalid refresh token provided");
        }

        // Extract username and retrieve user details
        String username = jwtService.extractUsername(refreshToken);
        User user = repository.findByEmail(username)
                .orElseThrow(() -> {
                    logger.error("No user found with email [{}] during token refresh", username);
                    return new NoSuchElementException("User not found with email: " + username);
                });

        // Generate new access token
        String newAccessToken = jwtService.generateToken(user);
        logger.info("Access token successfully refreshed for user [{}]", username);

        // Optionally, if refresh tokens also need to be rotated
        String newRefreshToken = jwtService.generateRefreshToken(user);

        // Build and return the response
        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)  // Consider whether to rotate refresh tokens here
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }

}
