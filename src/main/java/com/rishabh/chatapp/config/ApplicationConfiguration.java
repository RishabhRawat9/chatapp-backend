package com.rishabh.chatapp.config;

import com.rishabh.chatapp.repository.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration //-> this is for telling spring ki this is a place where i define a bunch of beans for the app
public class ApplicationConfiguration {
    private final UserRepo userRepository;

    public ApplicationConfiguration(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    UserDetailsService userDetailsService() { // since the interface has only one method we can use a lambda o/w we could implement a complete class and having a loadByUsername() method impl.
        // this is the same thing but with lambda.
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //so when the user logs in with email and password how do we authenticate, the authentication is done by the authentication manager which by default uses the providerManager (which gives a list of authentication providers that can be used to autherize the credentials by using the userDetailsservice(which has a loadByUsername method) which these providers call under the hood to authenticate the db data and the provided data (for login) and if the auth is successful then we create the jwt token and send it as response.


    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}