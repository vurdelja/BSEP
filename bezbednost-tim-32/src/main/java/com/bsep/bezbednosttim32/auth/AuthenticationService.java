package com.bsep.bezbednosttim32.auth;

import com.bsep.bezbednosttim32.config.JwtService;
import com.bsep.bezbednosttim32.model.RegistrationRequest;
import com.bsep.bezbednosttim32.model.Role;
import com.bsep.bezbednosttim32.model.User;
import com.bsep.bezbednosttim32.repository.RegistrationRequestRepository;
import com.bsep.bezbednosttim32.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final RegistrationRequestRepository requestRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request) {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail(request.getEmail());
        registrationRequest.setPassword(passwordEncoder.encode(request.getPassword()));

        requestRepository.save(registrationRequest);

        return AuthenticationResponse.builder()
                .message("Registration request sent for approval")
                .build();
    }

    public AuthenticationResponse approveRegistrationRequest(Integer requestId, RegisterRequest request) {
        RegistrationRequest regRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Registration request not found"));


        // Create a user based on the registration request and save it
        String validationMessage = validatePassword(request.getPassword(), request.getPasswordConfirm());
        if (validationMessage != null) {
            return AuthenticationResponse.builder()
                    .message(validationMessage)
                    .build();
        }

        var user = User.builder()
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
        repository.save(user);

        regRequest.setApproved(true);
        requestRepository.save(regRequest);

        return AuthenticationResponse.builder()
                .message("User registered successfully")
                .build();
    }


    public void rejectRegistrationRequest(Integer requestId) {
        // Find the registration request by ID
        RegistrationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Registration request not found"));

        // Mark the request as rejected or delete it
        requestRepository.delete(request);
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
        return null; // All validations passed
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken =jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(LogInRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken =jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
