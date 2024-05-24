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
import java.util.NoSuchElementException;
@RestController
@RequestMapping("/bsep/request")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService service;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/sendRequest")
    public ResponseEntity<?> sendRequest(@RequestBody RegisterRequest request) {
        try {
            System.out.println(request);
            return ResponseEntity.ok(service.sendRequest(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/approve/{requestId}")
    public ResponseEntity<ResponseEntity> approveRegistrationRequest(@PathVariable Integer requestId) {
        ResponseEntity response = service.approveRequest(requestId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam String token) {
        try {
            Request request = requestRepository.findByActivationToken(token)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid or expired activation token"));

            User newUser = createUserFromRequest(request);
            userRepository.save(newUser);

            return ResponseEntity.ok("Account activated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Activation failed: " + e.getMessage());
        }
    }

    private User createUserFromRequest(Request request) {
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword()) // Password is already hashed during request creation
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

    @PostMapping("/reject/{id}")
    public ResponseEntity<String> rejectRegistrationRequest(@PathVariable Integer id, @RequestBody Map<String, String> requestMap) {
        String rejectionReason = requestMap.get("reason");
        if (rejectionReason == null) {
            return ResponseEntity.badRequest().body("Rejection reason must be provided.");
        }
        try {
            String response = service.rejectRegistrationRequest(id, rejectionReason);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your request.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Request>> getAllRequests() {
        List<Request> requests = service.findAllRequests();
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }
}
