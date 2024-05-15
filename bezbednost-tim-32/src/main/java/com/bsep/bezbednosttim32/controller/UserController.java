package com.bsep.bezbednosttim32.controller;

import com.bsep.bezbednosttim32.auth.*;
import com.bsep.bezbednosttim32.service.AuthenticationService;
import com.bsep.bezbednosttim32.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService service;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(
            @RequestBody LoginRequest request
    ){
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(
            @RequestBody Map<String, String> request
    ) {
        String refreshToken = request.get("refreshToken");
        return ResponseEntity.ok(service.refreshAccessToken(refreshToken));
    }
    @GetMapping("/users/{userId}")
    public ResponseEntity<RegisterRequest> getUserDetails(@PathVariable Integer userId) {
        RegisterRequest userDetailsResponse = service.getUserDetails(userId);
        return ResponseEntity.ok(userDetailsResponse);
    }
    @PatchMapping("/users/{userId}")
    public ResponseEntity<Boolean> updateUserDetails(@PathVariable Integer userId, @RequestBody RegisterRequest userUpdateRequest) {
        boolean isUpdated = userService.updateUserDetails(userId, userUpdateRequest);
        return ResponseEntity.ok(isUpdated);
    }


}
