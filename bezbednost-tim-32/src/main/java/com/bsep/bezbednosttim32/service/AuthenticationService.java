package com.bsep.bezbednosttim32.service;

import com.bsep.bezbednosttim32.auth.LoginResponse;
import com.bsep.bezbednosttim32.auth.LoginRequest;
import com.bsep.bezbednosttim32.model.User;
import com.bsep.bezbednosttim32.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
        logger.info("Attempting to authenticate user with email: {}", request.getEmail());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            logger.error("Authentication failed for user with email: {}", request.getEmail(), e);
            throw e;
        }

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", request.getEmail());
                    return new NoSuchElementException("User not found");
                });

        logger.info("User authenticated successfully, generating tokens...");
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        logger.info("Tokens generated successfully for user with email: {}", request.getEmail());
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }

    public LoginResponse refreshAccessToken(String refreshToken) {
        logger.info("Attempting to refresh access token");
        if (jwtService.isTokenValid(refreshToken)) {
            var username = jwtService.extractUsername(refreshToken);
            var user = repository.findByEmail(username)
                    .orElseThrow(() -> {
                        logger.error("User not found with email extracted from refresh token: {}", username);
                        return new NoSuchElementException("User not found");
                    });
            var accessToken = jwtService.generateToken(user);
            logger.info("Access token refreshed successfully for user with email: {}", username);
            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            logger.error("Invalid refresh token");
            throw new IllegalArgumentException("Invalid refresh token");
        }
    }
}
