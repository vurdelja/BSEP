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

    private final AuthenticationService authenticationService;
    private final RequestService requestService;
    private final RequestRepository repository;


    @PostMapping("/approve/{requestId}")
    public ResponseEntity<LoginResponse> approveRegistrationRequest(
            @PathVariable Integer requestId,
            @RequestBody RegisterRequest request
    ) {
        LoginResponse response = authenticationService.approveRegistrationRequest(requestId, request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/reject/{id}")
    public ResponseEntity<Void> rejectRegistrationRequest(
            @PathVariable Integer id,
            @RequestBody Map<String, String> request) {
        String rejectionReason = request.get("reason");
        authenticationService.rejectRegistrationRequest(id, rejectionReason);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Request>> getAllRequests(){
        List<Request> requests = requestService.findAllRequests();
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Request> getRequestById(@PathVariable("id") Integer id){
        Request request = requestService.findRequestById(id);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PutMapping("/update")
    ResponseEntity<Request> updateRequest(@RequestBody Request request){
        Request updateRequest = requestService.updateRequest(request);
        return new ResponseEntity<>(updateRequest, HttpStatus.OK);
    }








}