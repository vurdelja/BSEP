package com.bsep.bezbednosttim32.service;

import com.bsep.bezbednosttim32.auth.LoginResponse;
import com.bsep.bezbednosttim32.auth.RegisterRequest;
import com.bsep.bezbednosttim32.exceptions.RequestNotFoundException;
import com.bsep.bezbednosttim32.model.Request;
import com.bsep.bezbednosttim32.model.RequestStatus;
import com.bsep.bezbednosttim32.model.Role;
import com.bsep.bezbednosttim32.model.User;
import com.bsep.bezbednosttim32.repository.RequestRepository;
import com.bsep.bezbednosttim32.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@AllArgsConstructor
public class RequestService {
    private final RequestRepository repository;
    private final UserRepository userRepository;

    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(RequestService.class);
    public ResponseEntity<?> sendRequest(RegisterRequest request) {
        String validationMessage = validatePassword(request.getPassword(), request.getPasswordConfirm());
        if (validationMessage != null) {
            return ResponseEntity.badRequest().body(validationMessage);
        }

        Request registrationRequest = new Request();
        registrationRequest.setEmail(request.getEmail());
        registrationRequest.setPassword(passwordEncoder.encode(request.getPassword()));
        registrationRequest.setAddress(request.getAddress());
        registrationRequest.setCity(request.getCity());
        registrationRequest.setCountry(request.getCountry());
        registrationRequest.setPhoneNumber(request.getPhoneNumber());
        registrationRequest.setUserType(request.getUserType());
        registrationRequest.setFirstName(request.getFirstName());
        registrationRequest.setLastName(request.getLastName());
        registrationRequest.setCompanyName(request.getCompanyName());
        registrationRequest.setPib(request.getPib());
        registrationRequest.setPackageType(request.getPackageType());
        registrationRequest.setStatus(RequestStatus.PENDING);

        addRequest(registrationRequest);

        return ResponseEntity.ok("Registration request sent for approval");
    }

    private String validatePassword(String password, String passwordConfirm) {
        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one number";
        }
        if (!password.equals(passwordConfirm)) {
            return "Passwords do not match";
        }
        return null;
    }


    public ResponseEntity<?> approveRequest(Integer requestId) {
        Request regRequest = findRequestById(requestId);


        // Set status to APPROVED
        regRequest.setStatus(RequestStatus.APPROVED);
        updateRequest(regRequest);

        // Generate activation token and create activation link
        String activationToken = generateActivationToken(regRequest);
        String activationLink = "http://localhost:8080/bsep/request/activate?token=" + activationToken;

        // Email body with activation link
        String emailBody = String.format("Dear %s,\n\n" +
                "Your registration has been approved. Please click the following link to activate your account:\n" +
                "%s\n\n" +
                "Best regards,\n" +
                "Your Company", regRequest.getFirstName(), activationLink);

        emailService.sendEmail(regRequest.getEmail(), "Registration Approved", emailBody);

        return ResponseEntity.ok("Request approved");
    }


    private String generateActivationToken(Request request) {
        String token = UUID.randomUUID().toString();
        request.setActivationToken(token);
        updateRequest(request);
        return token;
    }

    public String rejectRegistrationRequest(Integer requestId, String rejectionReason) {
        Request request = repository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Registration request not found"));

        request.setStatus(RequestStatus.REJECTED);
        updateRequest(request);

        String toEmail = request.getEmail();
        String subject = "Registration Request Rejected";
        String body = "Dear " + request.getEmail() + ",\n\n" +
                "Your registration request has been rejected for the following reason:\n" +
                rejectionReason + "\n\n" +
                "Best regards,\n" +
                "Your Company";

        emailService.sendEmail(toEmail, subject, body);

        return "Request rejected successfully.";
    }


    public List<Request> findAllRequests(){
        return repository.findAll();
    }

    public Request updateRequest(Request request){
        return repository.save(request);
    }


    public Request findRequestById(Integer id){
        return repository.findRequestById(id)
                .orElseThrow(()-> new RequestNotFoundException("Request by id" + id + "not found."));
    }

    public Request addRequest(Request request){
        return repository.save(request);
    }



}
