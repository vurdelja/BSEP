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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RequestService {
    private final RequestRepository repository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public LoginResponse sendRequest(RegisterRequest request) {

        String validationMessage = validatePassword(request.getPassword(), request.getPasswordConfirm());
        if (validationMessage != null) {
            return LoginResponse.builder()
                    .message(validationMessage)
                    .build();
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
        registrationRequest.setStatus(RequestStatus.PENDING); // Assuming there's a PENDING status for initial state

        addRequest(registrationRequest);

        return LoginResponse.builder()
                .message("Registration request sent for approval")
                .build();
    }


    public LoginResponse approveRequest(Integer requestId) {
        Request regRequest = findRequestById(requestId);


        var user = User.builder()
                .email(regRequest.getEmail())
                .password(passwordEncoder.encode(regRequest.getPassword()))
                .role(Role.USER)
                .address(regRequest.getAddress())
                .city(regRequest.getCity())
                .country(regRequest.getCountry())
                .phoneNumber(regRequest.getPhoneNumber())
                .userType(regRequest.getUserType())
                .firstName(regRequest.getFirstName())
                .lastName(regRequest.getLastName())
                .companyName(regRequest.getCompanyName())
                .pib(regRequest.getPib())
                .packageType(regRequest.getPackageType())
                .build();
        userRepository.save(user);

        regRequest.setStatus(RequestStatus.APPROVED);
        updateRequest(regRequest);

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
        Request request = repository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Registration request not found"));

        request.setStatus(RequestStatus.REJECTED);
        updateRequest(request);

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
