package com.rishabh.chatapp.service;


import com.rishabh.chatapp.entity.ChatStatus;
import com.rishabh.chatapp.entity.User;
import com.rishabh.chatapp.model.AddContactDto;
import com.rishabh.chatapp.model.ContactResponseDto;
import com.rishabh.chatapp.repository.ChatStatusRepo;
import com.rishabh.chatapp.repository.UserRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final ChatStatusRepo chatStatusRepo;
    public UserService(UserRepo userRepo, ChatStatusRepo chatStatusRepo){
        this.userRepo = userRepo;
        this.chatStatusRepo = chatStatusRepo;

    }


    public UUID addContact(AddContactDto dto){//also create a new entry in chatStatus table and return the id .
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

        Cuser.getContacts().add(User);
        userRepo.save(Cuser);

        String userEmail = User.getEmail();
        String friendEMail = Cuser.getEmail();


        UUID id = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        ChatStatus entry1 = ChatStatus.builder().id(id).sender(userEmail).receiver(friendEMail).build();
        ChatStatus entry2 = ChatStatus.builder().id(id2).sender(friendEMail).receiver(userEmail).build();
        chatStatusRepo.save(entry1);
        chatStatusRepo.save(entry2); //there will be two entries for two users,


        return id;
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
