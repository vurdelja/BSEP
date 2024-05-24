package com.bsep.bezbednosttim32.controller;

import com.bsep.bezbednosttim32.auth.LoginResponse;
import com.bsep.bezbednosttim32.service.AuthenticationService;
import com.bsep.bezbednosttim32.auth.RegisterRequest;
import com.bsep.bezbednosttim32.model.Request;
import com.bsep.bezbednosttim32.repository.RequestRepository;
import com.bsep.bezbednosttim32.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bsep/request")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService service;


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