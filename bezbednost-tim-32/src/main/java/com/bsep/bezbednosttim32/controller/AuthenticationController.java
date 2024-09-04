package com.bsep.bezbednosttim32.controller;

import com.bsep.bezbednosttim32.auth.LoginRequest;
import com.bsep.bezbednosttim32.auth.LoginResponse;
import com.bsep.bezbednosttim32.auth.RegisterRequest;
import com.bsep.bezbednosttim32.service.AuthenticationService;
import com.bsep.bezbednosttim32.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bsep/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService service;
    private final RegistrationService registrationService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = service.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Authentication failed: " + e.getMessage()));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(
            @RequestBody Map<String, String> request
    ) {
        String refreshToken = request.get("refreshToken");
        logger.info("Refresh token request received");
        return ResponseEntity.ok(service.refreshAccessToken(refreshToken));
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody RegisterRequest request) {
        try {
            Map<String, Object> response = registrationService.registerUser(request);
            if ("success".equals(response.get("status"))) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            logger.error("Registration failed", e); // Log the exception for debugging
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
