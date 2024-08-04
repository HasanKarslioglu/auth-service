package com.customer.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.customer.auth.model.User;
import com.customer.auth.service.AuthenticationService;


@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        String token = authenticationService.registerUser(user.getUsername(), user.getPassword());
        return token != null ? ResponseEntity.ok(token) : ResponseEntity.badRequest().body("Username Already Exist");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String token = authenticationService.loginUser(user.getUsername(), user.getPassword());
        return token != null ? ResponseEntity.ok(token) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    //@GetMapping("/verify")
    //public ResponseEntity<String> verifyToken(@RequestParam String token) {
    //    boolean isValid = authenticationService.verifyToken(token);
    //    return isValid ? ResponseEntity.ok("Token is valid") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    //}
}