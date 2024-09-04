package com.bsep.bezbednosttim32.controller;

import com.bsep.bezbednosttim32.auth.*;
import com.bsep.bezbednosttim32.model.User;
import com.bsep.bezbednosttim32.service.AuthenticationService;
import com.bsep.bezbednosttim32.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/bsep/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("/getById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }


    @PreAuthorize("hasAuthority('UPDATE_PERSONAL_DETAILS')")
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }


}
