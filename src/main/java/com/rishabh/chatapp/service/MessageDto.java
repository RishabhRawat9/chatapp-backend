package com.rishabh.chatapp.service;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    public String message;
    public String from;
    public String to;
    public Timestamp sentAt;

}
