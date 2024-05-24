package com.bsep.bezbednosttim32.controller;
import com.bsep.bezbednosttim32.auth.LoginResponse;
import com.bsep.bezbednosttim32.auth.RegisterRequest;
import com.bsep.bezbednosttim32.model.Request;
import com.bsep.bezbednosttim32.model.Role;
import com.bsep.bezbednosttim32.model.User;
import com.bsep.bezbednosttim32.repository.RequestRepository;
import com.bsep.bezbednosttim32.repository.UserRepository;
import com.bsep.bezbednosttim32.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bsep/request")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService service;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;  // Assuming you have a RequestRepository
    private final PasswordEncoder passwordEncoder;     // Make sure to inject PasswordEncoder


    @PostMapping("/sendRequest")
    public ResponseEntity<LoginResponse> sendRequest(@RequestBody RegisterRequest request) {
        try {
            System.out.println(request);
            return ResponseEntity.ok(service.sendRequest(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Registration failed: " + e.getMessage()));
        }
    }


    @PostMapping("/approve/{requestId}")
    public ResponseEntity<LoginResponse> approveRegistrationRequest(
            @PathVariable Integer requestId
    ) {
        LoginResponse response = service.approveRequest(requestId);
        return ResponseEntity.ok(response);
    }



    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam String token) {
        try {
            Request request = requestRepository.findByActivationToken(token)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid or expired activation token"));

            User newUser = createUserFromRequest(request);
            userRepository.save(newUser);
            service.updateRequest(request);  // Assuming RequestService has an updateRequest method

            return ResponseEntity.ok("Account activated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Activation failed: " + e.getMessage());
        }
    }


    private User createUserFromRequest(Request request) {
        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .phoneNumber(request.getPhoneNumber())
                .userType(request.getUserType())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .companyName(request.getCompanyName())
                .pib(request.getPib())
                .packageType(request.getPackageType())
                .build();
    }

    private Request validateTokenAndGetRequest(String token) {
        // Implement token validation and retrieval of request
        return requestRepository.findByActivationToken(token).orElseThrow(() -> new IllegalStateException("Invalid activation token."));
    }



    @PostMapping("/reject/{id}")
    public ResponseEntity<Void> rejectRegistrationRequest(
            @PathVariable Integer id,
            @RequestBody Map<String, String> request) {
        String rejectionReason = request.get("reason");
        service.rejectRegistrationRequest(id, rejectionReason);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Request>> getAllRequests(){
        List<Request> requests = service.findAllRequests();
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }







}