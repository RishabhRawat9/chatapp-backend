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
import java.util.UUID;

@Service
public class ChatService {
    private final MessageRepo messageRepo;
    private final UserRepo userRepo;
    public ChatService(MessageRepo messageRepo, UserRepo userRepo){
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;

    }

    public SimpleMsg saveMessage(SimpleMsg msg){
        System.out.println(msg.getFrom() +" "+msg.getTo());
        Optional<User> from =  userRepo.findByEmail(msg.getFrom());
        Optional<User> to =  userRepo.findByEmail(msg.getTo());
        if(from.isPresent()&& to.isPresent()){
            Message message = Message.builder().id(UUID.randomUUID()).message(msg.getMessage()).sender(from.get().getEmail()).receiver(to.get().getEmail()).fromUserId(from.get().getUserId()).toUserId(to.get().getUserId()).sentAt(msg.getSentAt()).build();
            messageRepo.save(message);
    //ok so now messages can be tracked through their id from the client, setup the ack delivery system for the client
            return msg;


        }
        else{
            throw new UsernameNotFoundException("invalid username while saving message");
        }

    }
}
