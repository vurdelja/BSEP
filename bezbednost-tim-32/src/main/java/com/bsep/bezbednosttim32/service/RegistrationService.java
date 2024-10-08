package com.bsep.bezbednosttim32.service;

import com.bsep.bezbednosttim32.auth.RegisterRequest;
import com.bsep.bezbednosttim32.model.Role;
import com.bsep.bezbednosttim32.model.User;
import com.bsep.bezbednosttim32.repository.RoleRepository;
import com.bsep.bezbednosttim32.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private static final Logger log = LoggerFactory.getLogger(RegistrationService.class);

    public Map<String, Object> registerUser(RegisterRequest request) throws Exception {
        log.debug("Registering new user with email: {}", request.getEmail());

        Map<String, Object> response = new HashMap<>();
        String validationMessage = validatePassword(request.getPassword(), request.getPasswordConfirm());

        if (validationMessage != null) {
            log.warn("Password validation failed for user {}: {}", request.getEmail(), validationMessage);
            response.put("status", "error");
            response.put("message", validationMessage);
            return response;
        }

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            log.warn("Registration attempt with used email: {}", request.getEmail());
            response.put("status", "error");
            response.put("message", "Email is already in use.");
            return response;
        }

        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(request.getPassword(), salt);

        // Fetch the 'USER' role from the database
        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            log.error("Role 'USER' not found in the database. Make sure the role is created.");
            throw new RuntimeException("Role 'USER' not found");
        }

        // Generating encryption key and IV for address encryption
        SecretKey encryptionKey = EncryptionUtils.generateAESKey();
        byte[] iv = new byte[12];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        // Encrypting the user's address
        String encryptedAddress = EncryptionUtils.encrypt(request.getAddress(), encryptionKey, iv);

        // Creating the user with encrypted address and setting the role
        User user = createUserFromRequest(request, hashedPassword);
        user.setEmail(request.getEmail());
        user.setAddress(encryptedAddress);
        user.setEncryptionKey(Base64.getEncoder().encodeToString(encryptionKey.getEncoded()));
        user.setIv(Base64.getEncoder().encodeToString(iv));
        user.setRoles(Set.of(userRole)); // Assigning the 'USER' role

        userRepository.save(user);
        log.info("User registered successfully with email: {}", request.getEmail());

        response.put("status", "success");
        response.put("message", "Registration successful.");
        return response;
    }


    private String generateQRCodeUrl(String secret, String email) {
        // Assuming your application's name is your 'issuer'
        String issuer = "BezbednostTim32";
        return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", issuer, email, secret, issuer);
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
                .password(hashedPassword)
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
