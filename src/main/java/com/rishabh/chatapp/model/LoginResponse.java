package com.rishabh.chatapp.model;

import lombok.Builder;

import java.util.Date;
import java.util.UUID;

@Builder
public class LoginResponse {

    public String token;

    public UUID userId;

    public String name;

}
