package com.rishabh.chatapp.model;

import com.rishabh.chatapp.entity.ChatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatHistory {
    public List<SimpleMsg> messages;
    public ChatStatus lastSeenMsg;

}
