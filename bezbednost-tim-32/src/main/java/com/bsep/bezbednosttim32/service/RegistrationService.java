package com.bsep.bezbednosttim32.service;

import com.bsep.bezbednosttim32.auth.RegisterRequest;
import com.bsep.bezbednosttim32.model.Role;
import com.bsep.bezbednosttim32.model.User;
import com.bsep.bezbednosttim32.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@AllArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(RegistrationService.class);
    public Map<String, Object> registerUser(RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();

        String validationMessage = validatePassword(request.getPassword(), request.getPasswordConfirm());
        if (validationMessage != null) {
            response.put("status", "error");
            response.put("message", validationMessage);
            return response;
        }

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            response.put("status", "error");
            response.put("message", "Email is already in use.");
            return response;
        }

        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(request.getPassword(), salt);

        User user = createUserFromRequest(request, hashedPassword);
        userRepository.save(user);

        response.put("status", "success");
        response.put("message", "Registration successful");
        return response;
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

    private User createUserFromRequest(RegisterRequest request, String hashedPassword) {
        return User.builder()
                .email(request.getEmail())
                .password(hashedPassword) // Store hashed password with salt
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
}
