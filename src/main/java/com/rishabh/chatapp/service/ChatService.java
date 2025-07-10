package com.rishabh.chatapp.service;


import com.rishabh.chatapp.controllers.ChatController;
import com.rishabh.chatapp.entity.Message;
import com.rishabh.chatapp.entity.User;
import com.rishabh.chatapp.model.SimpleMsg;
import com.rishabh.chatapp.repository.MessageRepo;
import com.rishabh.chatapp.repository.UserRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatService {
    private final MessageRepo messageRepo;
    private final UserRepo userRepo;
    public ChatService(MessageRepo messageRepo, UserRepo userRepo){
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;

    }

    public void saveMessage(SimpleMsg msg){
        System.out.println(msg.getFrom() +" "+msg.getTo());
        Optional<User> from =  userRepo.findByEmail(msg.getFrom());
        Optional<User> to =  userRepo.findByEmail(msg.getTo());
        if(from.isPresent()&& to.isPresent()){
            Message message = Message.builder().message(msg.getMessage()).from(from.get().getEmail()).to(to.get().getEmail()).fromUserId(from.get().getUserId()).toUserId(to.get().getUserId()).build();
            messageRepo.save(message);
            System.out.println("message saved");

        }
        else{
            throw new UsernameNotFoundException("invalid username while saving message");
        }

    }
}
