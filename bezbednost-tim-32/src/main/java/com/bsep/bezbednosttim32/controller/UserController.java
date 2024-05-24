package com.bsep.bezbednosttim32.controller;

import com.bsep.bezbednosttim32.auth.*;
import com.bsep.bezbednosttim32.service.AuthenticationService;
import com.bsep.bezbednosttim32.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/bsep/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService service;
    private final UserService userService;


    @GetMapping("/getUser/{userId}")
    public ResponseEntity<RegisterRequest> getUserDetails(@PathVariable Integer userId) {
        RegisterRequest userDetailsResponse = userService.getUserDetails(userId);
        return ResponseEntity.ok(userDetailsResponse);
    }
    @PatchMapping("/updateUser/{userId}")
    public ResponseEntity<Boolean> updateUserDetails(@PathVariable Integer userId, @RequestBody RegisterRequest userUpdateRequest) {
        boolean isUpdated = userService.updateUserDetails(userId, userUpdateRequest);
        return ResponseEntity.ok(isUpdated);
    }


}
