package com.rishabh.chatapp.service;


import com.rishabh.chatapp.entity.User;
import com.rishabh.chatapp.model.LoginRequest;
import com.rishabh.chatapp.model.SignupRequest;
import com.rishabh.chatapp.repository.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService {
    private final UserRepo userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepo userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(SignupRequest input) {
        UUID uuid = UUID.randomUUID();
        User user = User.builder()
                .userId(uuid)
                .name(input.name())
                .password(passwordEncoder.encode(input.password()))
                .email(input.email()).build();
        return userRepository.save(user);
    }

    public User authenticate(LoginRequest input) {
        authenticationManager.authenticate(//this internally uses the daoauthprovider and calls the laodbyusername method and gets the details from db and mathches them with the provided details if matched we setup the security context.
                new UsernamePasswordAuthenticationToken(
                        input.email(),
                        input.password()
                )
        );
        return userRepository.findByEmail(input.email())
                .orElseThrow();
    }
}