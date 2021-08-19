package com.ippon.sprintbootbankapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class NotificationFactory {

    private EmailService defaultNotificationService;

    @Autowired
    private Map<String, NotificationService> notificationServices;

    public NotificationFactory(EmailService defaultNotificationService) {
        this.defaultNotificationService = defaultNotificationService;
    }

    public Optional<NotificationService> getPreferredService(String name) {
        return Optional.ofNullable(notificationServices.get(name));

    }

    public NotificationService getDefaultNotification() {
        return defaultNotificationService;
    }

}
