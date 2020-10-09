package com.ippon.bankapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("sms")
public class SmsService implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(SmsService.class);
    public static final String SMS = "sms";

    @Override
    public void sendMessage(String source, String destination, String subject, String message) {
        // don't implement, mock only
        log.info("Sent message:\n\tfrom: {} \n\tto: {}\n\tsubject: {}\n\tmessage: {}", source, destination, subject, message);
    }

    @Override
    public String getName() {
        return SMS;
    }
}
