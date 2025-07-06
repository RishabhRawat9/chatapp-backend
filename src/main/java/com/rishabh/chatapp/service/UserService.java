package com.rishabh.chatapp.service;


import com.rishabh.chatapp.entity.User;
import com.rishabh.chatapp.model.AddContactDto;
import com.rishabh.chatapp.model.ContactResponseDto;
import com.rishabh.chatapp.repository.UserRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;

@Service
public class UserService {

    private final UserRepo userRepo;
    public UserService(UserRepo userRepo){
        this.userRepo = userRepo;
    }


    public String addContact(AddContactDto dto){
        //ok so validate the contact through the contactId.
        Optional<User> contactUser =  userRepo.findByEmail(dto.getEmail().trim());
        if(contactUser.isEmpty()){
            throw new UsernameNotFoundException("invalid  contact email");
        }
        Optional<User> currUser =  userRepo.findByUserId(dto.getUserId());
        if(currUser.isEmpty()){
            throw new UsernameNotFoundException("invalid  userId");
        }
        //need to add them to each other contact lists;
        User Cuser = contactUser.get();
        User User = currUser.get();
        User.getContacts().add(Cuser);
        userRepo.save(User);
        return "contact added";
    }

    public Set<ContactResponseDto> getContacts(UUID userId){
        Optional<User> user = userRepo.findByUserId(userId);
        if(user.isEmpty()){
            throw new RuntimeException("invalid userId");
        }
        User currUser = user.get();
        List<ContactResponseDto> list=
    currUser.getContacts().stream().map(el -> ContactResponseDto.builder().email(el.getEmail())
                .name(el.getName()).build()
        ).toList();
        return new HashSet<>(list);
    }

}
