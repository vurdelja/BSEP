package com.bsep.bezbednosttim32.service;

import com.bsep.bezbednosttim32.model.User;
import com.bsep.bezbednosttim32.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;

    public User getUserWithDecryptedAddress(String email) {
        // Pronađi korisnika po email-u
        User user = repository.findByEmail(email).orElseThrow(() ->
                new NoSuchElementException("User not found"));

        try {
            // Dohvati šifrovani ključ i IV iz baze
            byte[] decryptionKeyBytes = Base64.getDecoder().decode(user.getEncryptionKey());
            SecretKey decryptionKey = new SecretKeySpec(decryptionKeyBytes, "AES");
            byte[] iv = Base64.getDecoder().decode(user.getIv());

            // Dešifruj adresu
            String decryptedAddress = EncryptionUtils.decrypt(user.getAddress(), decryptionKey, iv);
            logger.info("Decrypted address: [{}]", decryptedAddress);

            // Postavi dešifrovanu adresu nazad u objekat korisnika (ako je potrebno koristiti)
            user.setAddress(decryptedAddress);

            return user;

        } catch (Exception e) {
            logger.error("Failed to decrypt address for user with email: [{}], Error: {}", email, e.getMessage());
            throw new RuntimeException("Failed to decrypt address", e);
        }
    }


    public User updateUser(Integer id, User user) {
        User existingUser = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        logger.info("Updating user with ID: {}", id);

        // Update only the personal information fields
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setCompanyName(user.getCompanyName());
        existingUser.setPib(user.getPib());
        existingUser.setAddress(user.getAddress());
        existingUser.setCity(user.getCity());
        existingUser.setCountry(user.getCountry());
        existingUser.setPhoneNumber(user.getPhoneNumber());

        User updatedUser = repository.save(existingUser);
        logger.info("User with ID: {} updated successfully", id);
        return updatedUser;
    }

    public User findUserById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
