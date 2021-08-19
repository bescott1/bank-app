package com.ippon.sprintbootbankapp.service;


public interface NotificationService {

    void sendMessage(String source, String destination, String subject, String message);

    String getName();
}
