package com.rishabh.chatapp.controllers;


import com.rishabh.chatapp.entity.Message;
import com.rishabh.chatapp.model.SimpleMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void handleChat(SimpleMsg msg){
        System.out.println("message is here");
        messagingTemplate.convertAndSend("/topic/chat", msg);
    }
}
