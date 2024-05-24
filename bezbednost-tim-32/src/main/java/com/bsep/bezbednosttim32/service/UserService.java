package com.bsep.bezbednosttim32.service;
import com.bsep.bezbednosttim32.auth.RegisterRequest;
import com.bsep.bezbednosttim32.model.User;
import com.bsep.bezbednosttim32.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository userRepository) {
        this.repository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        repository.findAll().forEach(users::add);

        return users;
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


    public boolean updateUserDetails(Integer userId, RegisterRequest userUpdateRequest) {
        // Pronalaženje korisnika po ID-ju
        User existingUser = repository.findById(userId).orElse(null);

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
        repository.save(existingUser);

        return true; // Vraćanje true ako je ažuriranje uspešno
    }
}