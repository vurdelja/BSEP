package com.bsep.bezbednosttim32.controller;

import com.bsep.bezbednosttim32.auth.LoginRequest;
import com.bsep.bezbednosttim32.auth.LoginResponse;
import com.bsep.bezbednosttim32.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/bsep/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(
            @RequestBody LoginRequest request
    ) {
        logger.info("Login request received for email: {}", request.getEmail());
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(
            @RequestBody Map<String, String> request
    ) {
        String refreshToken = request.get("refreshToken");
        logger.info("Refresh token request received");
        return ResponseEntity.ok(service.refreshAccessToken(refreshToken));
    }
}
