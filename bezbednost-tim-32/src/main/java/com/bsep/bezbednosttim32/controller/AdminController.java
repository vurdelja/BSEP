package com.bsep.bezbednosttim32.controller;

import com.bsep.bezbednosttim32.auth.LoginResponse;
import com.bsep.bezbednosttim32.service.AuthenticationService;
import com.bsep.bezbednosttim32.auth.RegisterRequest;
import com.bsep.bezbednosttim32.model.RegistrationRequest;
import com.bsep.bezbednosttim32.repository.RegistrationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<LoginResponse> approveRegistrationRequest(
            @PathVariable Integer requestId,
            @RequestBody RegisterRequest request
    ) {
        LoginResponse response = authenticationService.approveRegistrationRequest(requestId, request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/reject-registration-request/{id}")
    public ResponseEntity<Void> rejectRegistrationRequest(
            @PathVariable Integer id,
            @RequestBody Map<String, String> request) {
        String rejectionReason = request.get("reason");
        authenticationService.rejectRegistrationRequest(id, rejectionReason);
        return ResponseEntity.noContent().build();
    }
}