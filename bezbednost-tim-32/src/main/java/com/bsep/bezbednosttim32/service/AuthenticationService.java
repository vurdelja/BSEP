package com.bsep.bezbednosttim32.service;

import com.bsep.bezbednosttim32.auth.LoginResponse;
import com.bsep.bezbednosttim32.auth.LoginRequest;
import com.bsep.bezbednosttim32.auth.RegisterRequest;
import com.bsep.bezbednosttim32.model.Request;
import com.bsep.bezbednosttim32.model.RequestStatus;
import com.bsep.bezbednosttim32.model.Role;
import com.bsep.bezbednosttim32.model.User;
import com.bsep.bezbednosttim32.repository.RequestRepository;
import com.bsep.bezbednosttim32.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    private final AuthenticationManager authenticationManager;


    private final JwtService jwtService;




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
