package com.ippon.bankapp.service;


public interface NotificationService {

    void sendMessage(String source, String destination, String subject, String message);

    String getName();
}
