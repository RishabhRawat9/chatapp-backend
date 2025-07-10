package com.rishabh.chatapp.service;


import com.rishabh.chatapp.entity.Message;
import com.rishabh.chatapp.entity.User;
import com.rishabh.chatapp.repository.MessageRepo;
import com.rishabh.chatapp.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {
    private MessageRepo messageRepo;
    private UserRepo  userRepo;
    public MessageService(MessageRepo messageRepo, UserRepo userRepo){
        this.messageRepo = messageRepo;
        this.userRepo= userRepo;
    }

    public List<Message> getMessages(String user, String friend){
        Optional<User> username= userRepo.findByEmail(user);
        Optional<User> friendname= userRepo.findByEmail(friend);

        if(username.isPresent()&& friendname.isPresent()){
            UUID uid = username.get().getUserId();
            UUID fid = friendname.get().getUserId();//only 1 week before messages i am initially fetching.
            Timestamp time = Timestamp.from(Instant.now().minus(7, ChronoUnit.DAYS));
            List<Message> messages = messageRepo.getHistory(uid, fid,time);
            return messages;
        }
        throw new RuntimeException("invalid");
    }
}
