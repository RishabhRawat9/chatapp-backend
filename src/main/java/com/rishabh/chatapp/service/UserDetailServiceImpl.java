package com.rishabh.chatapp.service;

import com.rishabh.chatapp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import com.rishabh.chatapp.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("not found username");

        }
        return user.get();

    }
}
