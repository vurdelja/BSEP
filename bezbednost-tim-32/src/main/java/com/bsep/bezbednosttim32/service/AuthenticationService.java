package com.bsep.bezbednosttim32.service;

import com.bsep.bezbednosttim32.auth.LoginResponse;
import com.bsep.bezbednosttim32.auth.LoginRequest;
import com.bsep.bezbednosttim32.model.User;
import com.bsep.bezbednosttim32.model.Role;
import com.bsep.bezbednosttim32.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jboss.aerogear.security.otp.Totp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        // Autentifikacija korisnika
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // Generisanje access i refresh tokena
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getRoles().stream().map(Role::getName).collect(Collectors.joining(",")))
                .userId(user.getId())
                .build();
    }

    public LoginResponse refreshAccessToken(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new SecurityException("Invalid refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);
        var user = repository.findByEmail(username)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // Generiši novi access token, opcionalno novi refresh token
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .role(user.getRoles().stream().map(Role::getName).collect(Collectors.joining(",")))
                .userId(user.getId())
                .build();
    }
}
