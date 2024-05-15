package com.bsep.bezbednosttim32.service;
import com.bsep.bezbednosttim32.auth.RegisterRequest;
import com.bsep.bezbednosttim32.model.User;
import com.bsep.bezbednosttim32.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }
    public boolean updateUserDetails(Integer userId, RegisterRequest userUpdateRequest) {
        // Pronalaženje korisnika po ID-ju
        User existingUser = userRepository.findById(userId).orElse(null);

        // Provera da li je korisnik pronađen
        if (existingUser == null) {
            return false; // Ako korisnik nije pronađen, vratite false
        }

        // Ažuriranje podataka korisnika (osim email-a)
        existingUser.setPassword(userUpdateRequest.getPassword());
        existingUser.setPasswordConfirm(userUpdateRequest.getPasswordConfirm());
        existingUser.setAddress(userUpdateRequest.getAddress());
        existingUser.setCity(userUpdateRequest.getCity());
        existingUser.setCountry(userUpdateRequest.getCountry());
        existingUser.setFirstName(userUpdateRequest.getFirstName());
        existingUser.setLastName(userUpdateRequest.getLastName());
        existingUser.setCompanyName(userUpdateRequest.getCompanyName());
        existingUser.setPib(userUpdateRequest.getPib());

        // Snimanje ažuriranih podataka u bazu
        userRepository.save(existingUser);

        return true; // Vraćanje true ako je ažuriranje uspešno
    }
}