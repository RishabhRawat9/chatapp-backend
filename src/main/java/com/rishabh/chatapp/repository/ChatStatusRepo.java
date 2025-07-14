package com.rishabh.chatapp.repository;

import com.rishabh.chatapp.entity.ChatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ChatStatusRepo extends JpaRepository<ChatStatus, UUID> {


    @Query("""
            SELECT m FROM ChatStatus m
            WHERE m.sender = :email1 AND m.receiver = :email2""")
    Optional<ChatStatus> findChatByEmails(String email1, String email2);//get the chat


}
