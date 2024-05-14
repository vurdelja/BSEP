package com.bsep.bezbednosttim32.controller;

import com.bsep.bezbednosttim32.auth.AuthenticationResponse;
import com.bsep.bezbednosttim32.auth.AuthenticationService;
import com.bsep.bezbednosttim32.auth.RegisterRequest;
import com.bsep.bezbednosttim32.model.RegistrationRequest;
import com.bsep.bezbednosttim32.repository.RegistrationRequestRepository;
import com.bsep.bezbednosttim32.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AdminController {

    private final AuthenticationService authenticationService;
    private final RegistrationRequestRepository repository;

    @GetMapping("/registration-requests")
    public ResponseEntity<List<RegistrationRequest>> getRegistrationRequests() {
        List<RegistrationRequest> requests = repository.findAll();
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/approve-registration-request/{requestId}")
    public ResponseEntity<AuthenticationResponse> approveRegistrationRequest(
            @PathVariable Integer requestId,
            @RequestBody RegisterRequest request
    ) {
        AuthenticationResponse response = authenticationService.approveRegistrationRequest(requestId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reject-registration-request/{requestId}")
    public ResponseEntity<?> rejectRegistrationRequest(@PathVariable Integer requestId) {
        authenticationService.rejectRegistrationRequest(requestId);
        return ResponseEntity.ok().build();
    }
}