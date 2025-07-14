package com.rishabh.chatapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "chat_status")
public class ChatStatus {

    @Id
    private UUID id;
    private String sender;
    private String receiver;
    private UUID lastSeenMsg;
    private Instant seenAt;
    private Instant updatedAt;
}
