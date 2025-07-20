package com.rishabh.chatapp.controllers;


import com.rishabh.chatapp.entity.ChatStatus;
import com.rishabh.chatapp.entity.Message;
import com.rishabh.chatapp.model.SimpleMsg;
import com.rishabh.chatapp.repository.ChatStatusRepo;
import com.rishabh.chatapp.repository.MessageRepo;
import com.rishabh.chatapp.repository.UserRepo;
import com.rishabh.chatapp.service.ChatService;
import com.rishabh.chatapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final MessageRepo messageRepo;
    private final ChatStatusRepo chatStatusRepo;


    public ChatController(ChatService chatService, MessageRepo messageRepo, ChatStatusRepo chatStatusRepo) {
        this.chatService = chatService;
        this.messageRepo = messageRepo;
        this.chatStatusRepo = chatStatusRepo;

    }

    @MessageMapping("/ping")
    public void handlePing(SimpleMsg ping){
        //ok so a ping is fired from a client, now we gotta check the msg and send this ping to the correct person.
        System.out.println("message is here "+ ping.getMessage());
        messagingTemplate.convertAndSend("/topic/private/"+ping.getTo(), ping);
    }
   @MessageMapping("/pong")
    public void handlePong(SimpleMsg pong){
        //ok so a ping is fired from a client, now we gotta check the msg and send this ping to the correct person.
       System.out.println("message is here "+ pong.getMessage());
        messagingTemplate.convertAndSend("/topic/private/"+pong.getTo(), pong);
    }

    @MessageMapping("/ack")
    public void handleAck(SimpleMsg msg) {

        //now this is a seen event, that i gotta handle and update the state for all other messages
        //get the message id, fromid ,toid, based on these three
        System.out.println(msg.getFrom() +"from -----   "+ msg.getTo() +"-->to  ");
        Optional<ChatStatus> reciept = chatStatusRepo.findChatByEmails(msg.getFrom(), msg.getTo());
        //ok so i have my chatStatus now, i gotta update it according to the last message.
        if(reciept.isEmpty()) throw new RuntimeException("not found the chat");

        //hey so see if i say that the chat b/w these two users would have only one entry then there would be a problem because i would be trying to sync the seen messages for both the participants with just one entry right.

        ChatStatus obj = reciept.get();
        //if for the messages the seen is set already then i donn't have to set them again.

        System.out.println(msg.getId());
        obj.setLastSeenMsg(msg.getId());
        obj.setSeenAt(msg.getSeenAt());
        obj.setUpdatedAt(Instant.now());
        chatStatusRepo.save(obj);

        messagingTemplate.convertAndSend( "/topic/receipt", obj);

        //now i am ready to send this receipt to the sender
    }

    @MessageMapping("/chat")
//jo mera client hai wo direct kis topic me push nhi kr rha mera server is deciding based on the msg headers who to send this message to.
    public void handleChat(SimpleMsg msg) {
        System.out.println("message is here");
//        messagingTemplate.convertAndSend("/topic/chat", msg);
        //1. parse the message => who is it being sent to -> send to that persons private incoming messages topic
        String pushToTopic = msg.getTo();//jo msg mere pe aaya hai vo kispe jana chaiye.
        System.out.println(pushToTopic);
        msg = chatService.saveMessage(msg);

        messagingTemplate.convertAndSend("/topic/private/" + pushToTopic, msg);
        messagingTemplate.convertAndSend("/topic/private/" + msg.getFrom(), msg);


        //now i have their emails and based on their emails i can get all teh info i need and add this to my message tables;
        //build the message object and then save.

    }

}
