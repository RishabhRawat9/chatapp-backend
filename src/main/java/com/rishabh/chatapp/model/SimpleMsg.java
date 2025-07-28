package com.rishabh.chatapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMsg {
    public UUID id;
    public String message;
    public String from;
    public String to;
    public Instant sentAt;
    public Instant deliveredAt;
    public Instant seenAt;
    public String type;




}
