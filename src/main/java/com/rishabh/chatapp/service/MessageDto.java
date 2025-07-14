package com.rishabh.chatapp.service;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    public UUID id;
    public String message;
    public String from;
    public String to;
    public LocalDateTime sentAt;
    public LocalDateTime deliveredAt;




}
