package com.bsep.bezbednosttim32.service;

import com.bsep.bezbednosttim32.auth.LoginResponse;
import com.bsep.bezbednosttim32.auth.LoginRequest;
import com.bsep.bezbednosttim32.auth.RegisterRequest;
import com.bsep.bezbednosttim32.model.RegistrationRequest;
import com.bsep.bezbednosttim32.model.RequestStatus;
import com.bsep.bezbednosttim32.model.Role;
import com.bsep.bezbednosttim32.model.User;
import com.bsep.bezbednosttim32.repository.RegistrationRequestRepository;
import com.bsep.bezbednosttim32.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final RegistrationRequestRepository requestRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

    public LoginResponse register(RegisterRequest request) {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail(request.getEmail());
        registrationRequest.setPassword(passwordEncoder.encode(request.getPassword()));

        requestRepository.save(registrationRequest);

        return LoginResponse.builder()
                .message("Registration request sent for approval")
                .build();
    }

    public LoginResponse approveRegistrationRequest(Integer requestId, RegisterRequest request) {
        RegistrationRequest regRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Registration request not found"));

        // Kreira usera na osnovu zahteva za registraciju i cuva ga
        String validationMessage = validatePassword(request.getPassword(), request.getPasswordConfirm());
        if (validationMessage != null) {
            return LoginResponse.builder()
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

        regRequest.setStatus(RequestStatus.APPROVED);
        requestRepository.save(regRequest);

        // Create and send the approval email with an activation link
        String activationLink = "http://yourdomain.com/activate?token=" + generateActivationToken(user);
        String emailBody = "Dear " + user.getFirstName() + ",\n\n"
                + "Your registration has been approved. Please click the following link to activate your account:\n"
                + activationLink + "\n\n"
                + "Best regards,\n"
                + "Your Company";

        emailService.sendEmail(user.getEmail(), "Registration Approved", emailBody);

        return LoginResponse.builder()
                .message("User approved for registration")
                .build();
    }

    private String generateActivationToken(User user) {
        // Generate an activation token (e.g., JWT or a unique UUID)
        // This is a placeholder; you need to implement token generation logic
        return UUID.randomUUID().toString();
    }
    public void rejectRegistrationRequest(Integer requestId, String rejectionReason) {
        RegistrationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Registration request not found"));

        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);

        String toEmail = request.getEmail();  // Assuming RegistrationRequest has a getEmail() method
        String subject = "Registration Request Rejected";
        String body = "Dear " + request.getEmail() + ",\n\n" + // Assuming RegistrationRequest has getFirstName()
                "Your registration request has been rejected for the following reason:\n" +
                rejectionReason + "\n\n" +
                "Best regards,\n" +
                "Your Company";

        emailService.sendEmail(toEmail, subject, body);
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

    //autentifikuje korisnika, generiše novi JWT access token i refresh token, i vraća ih kao odgovor
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    // proverava validnost refresh tokena, ekstraktuje korisničko ime iz tokena, traži korisnika u bazi i generiše novi access token
    public LoginResponse refreshAccessToken(String refreshToken) {
        if (jwtService.isTokenValid(refreshToken)) {
            var username = jwtService.extractUsername(refreshToken);
            var user = repository.findByEmail(username)
                    .orElseThrow(() -> new NoSuchElementException("User not found"));
            var accessToken = jwtService.generateToken(user);
            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new IllegalArgumentException("Invalid refresh token");
        }
    }
    public RegisterRequest getUserDetails(Integer userId) {
        // Dohvati korisnika iz baze podataka na osnovu ID-a
        User user = repository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // Kreirajte objekat UserDetailsResponse sa podacima o korisniku
        RegisterRequest userDetailsResponse = new RegisterRequest();
        userDetailsResponse.setId(user.getId());
        userDetailsResponse.setEmail(user.getEmail());
        userDetailsResponse.setPassword(user.getPassword());
        userDetailsResponse.setPasswordConfirm(user.getPasswordConfirm());
        userDetailsResponse.setAddress(user.getAddress());
        userDetailsResponse.setCity(user.getCity());
        userDetailsResponse.setCountry(user.getCountry());
        userDetailsResponse.setPhoneNumber(user.getPhoneNumber());
        userDetailsResponse.setUserType(user.getUserType());
        userDetailsResponse.setFirstName(user.getFirstName());
        userDetailsResponse.setLastName(user.getLastName());
        userDetailsResponse.setCompanyName(user.getCompanyName());
        userDetailsResponse.setPib(user.getPib());
        userDetailsResponse.setPackageType(user.getPackageType());

        // Postavite ostale podatke prema potrebama vaše aplikacije

        return userDetailsResponse;
    }




}
