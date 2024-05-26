package com.bsep.bezbednosttim32.service;

import com.bsep.bezbednosttim32.model.User;
import com.bsep.bezbednosttim32.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;

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
