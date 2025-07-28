package com.rishabh.chatapp.controllers;

import com.rishabh.chatapp.entity.ChatStatus;
import com.rishabh.chatapp.entity.Message;
import com.rishabh.chatapp.entity.User;
import com.rishabh.chatapp.model.AddContactDto;
import com.rishabh.chatapp.model.ChatHistory;
import com.rishabh.chatapp.model.SimpleMsg;
import com.rishabh.chatapp.repository.ChatStatusRepo;
import com.rishabh.chatapp.service.MessageDto;
import com.rishabh.chatapp.service.MessageService;
import com.rishabh.chatapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ContactController {
    //a user sends in a request to add another user into their contacts so the req comes in with a userid and we verify if the user exists if they do then we save them into each other contact and send a contact list back to the user.
    private final UserService userService;
    private final MessageService messageService;
    private final ChatStatusRepo chatStatusRepo;

    public ContactController(UserService userService, ChatStatusRepo chatStatusRepo, MessageService messageService) {
        this.chatStatusRepo = chatStatusRepo;
        this.userService = userService;
        this.messageService = messageService;

    }

    @PostMapping("/addContact")
    public ResponseEntity<?> addContact(@RequestBody @Valid AddContactDto dto) {
        try {
            return new ResponseEntity<>(userService.addContact(dto), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getContacts/{userId}")
    public ResponseEntity<?> getContacts(@PathVariable UUID userId) {
        try {
            return new ResponseEntity<>(userService.getContacts(userId), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/history/{user}/{friend}")
    public ResponseEntity<?> getChatHistory(@PathVariable String user, @PathVariable String friend) {
        List<Message> messages = messageService.getMessages(user, friend);
        Optional<ChatStatus> status_entry = chatStatusRepo.findChatByEmails(user, friend);
        UUID last_msg;
        if (status_entry.isPresent()) {
            last_msg = status_entry.get().getLastSeenMsg();
            ChatStatus seenMsg = status_entry.get();
                    List<SimpleMsg> list = messages.stream().map(el -> SimpleMsg.builder().id(el.getId()).message(el.getMessage()).from(el.getSender()).to(el.getReceiver()).sentAt(el.getSentAt()).build()).toList();

            ChatHistory sendObj = ChatHistory.builder().messages(list).lastSeenMsg(seenMsg).build();
            return new ResponseEntity<>(sendObj, HttpStatus.OK);
            //also send the chatStatus for this chat

        } else {
            throw new RuntimeException("not found the chatStatus");
        }


    }


}
