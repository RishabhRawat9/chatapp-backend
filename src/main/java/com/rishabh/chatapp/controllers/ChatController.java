package com.rishabh.chatapp.controllers;


import com.rishabh.chatapp.entity.Message;
import com.rishabh.chatapp.model.SimpleMsg;
import com.rishabh.chatapp.repository.MessageRepo;
import com.rishabh.chatapp.repository.UserRepo;
import com.rishabh.chatapp.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;


    public ChatController(ChatService chatService){
       this.chatService = chatService;
    }

    @MessageMapping("/chat")//jo mera client hai wo direct kis topic me push nhi kr rha mera server is deciding based on the msg headers who to send this message to.
    public void handleChat(SimpleMsg msg){
        System.out.println("message is here");
//        messagingTemplate.convertAndSend("/topic/chat", msg);

        //now every connected user will send a message here and it's the controllers job to classify these messages to every users private inbox topic,
        //every user has their incoming message topic to which they subscribe to when they make the ws connection, now all their messages are in this queue it is based on the info in messages like from and to that these messages will be classified both on the server side and the client side.

        //1. parse the message => who is it being sent to -> send to that persons private incoming messages topic

        String pushToTopic = msg.getTo();//jo msg mere pe aaya hai vo kispe jana chaiye.
        System.out.println(pushToTopic);
        messagingTemplate.convertAndSend("/topic/private/"+pushToTopic,msg);
        messagingTemplate.convertAndSend("/topic/private/"+msg.getFrom(),msg);


        chatService.saveMessage(msg);
        //now i have their emails and based on their emails i can get all teh info i need and add this to my message tables;
        //build the message object and then save.

    }
}
