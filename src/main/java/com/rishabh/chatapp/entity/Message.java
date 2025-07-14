package com.rishabh.chatapp.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "chat_messages")
public class Message {
    @Id

    private UUID id;
    private UUID fromUserId;
    private UUID toUserId;
    private String message;
    private String sender;
    private String receiver;
    @Column(name = "sent_at", columnDefinition = "TIMESTAMPTZ")
    private Instant sentAt;

    @Column(name = "delivered_at", columnDefinition = "TIMESTAMPTZ")
    private Instant deliveredAt;

    private UUID groupId;

}
