package com.rishabh.chatapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
public class ChatappApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));//was having postgres timezone issues so need to set the jvm timezone explicitly.
		SpringApplication.run(ChatappApplication.class, args);
		System.out.println("JVM TimeZone: " + ZoneId.systemDefault());
	}

}
