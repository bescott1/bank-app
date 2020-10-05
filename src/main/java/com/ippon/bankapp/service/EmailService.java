package com.ippon.bankapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    public static final String EMAIL = "email";

    @Override
    public void sendMessage(String source, String destination, String subject, String message) {
        // don't implement, mock only
        log.info("Sent message:\n\tfrom: {} \n\tto: {}\n\tsubject: {}\n\tmessage: {}", source, destination, subject, message);
    }

    @Override
    public String getName() {
        return EMAIL;
    }
}
