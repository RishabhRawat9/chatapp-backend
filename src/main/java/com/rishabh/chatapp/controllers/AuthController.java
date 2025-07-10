package com.rishabh.chatapp.controllers;

import com.rishabh.chatapp.entity.User;
import com.rishabh.chatapp.model.LoginRequest;
import com.rishabh.chatapp.model.LoginResponse;
import com.rishabh.chatapp.model.SignupRequest;
import com.rishabh.chatapp.service.AuthenticationService;
import com.rishabh.chatapp.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class AuthController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody SignupRequest registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse =  LoginResponse.builder().email(authenticatedUser.getEmail()).userId(authenticatedUser.getUserId()).token(jwtToken).name(authenticatedUser.getName()).build();
        return ResponseEntity.ok(loginResponse);
    }
}