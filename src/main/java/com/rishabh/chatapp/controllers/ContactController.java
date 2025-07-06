package com.rishabh.chatapp.controllers;

import com.rishabh.chatapp.entity.User;
import com.rishabh.chatapp.model.AddContactDto;
import com.rishabh.chatapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ContactController {
    //a user sends in a request to add another user into their contacts so the req comes in with a userid and we verify if the user exists if they do then we save them into each other contact and send a contact list back to the user.
    private final UserService userService;
    public ContactController(UserService userService){
        this.userService =userService;

    }

    @PostMapping("/addContact")
    public ResponseEntity<?> addContact(@RequestBody @Valid AddContactDto dto){
        try{
            return new ResponseEntity<>(userService.addContact(dto), HttpStatus.OK);
        }
        catch (Exception ex){
            return  new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getContacts/{userId}")
    public ResponseEntity<?>getContacts(@PathVariable UUID userId){
        try{
            return new ResponseEntity<>(userService.getContacts(userId), HttpStatus.OK);
        }
        catch (Exception ex){
            return  new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
