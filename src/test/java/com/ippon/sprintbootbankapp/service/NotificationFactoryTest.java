package com.ippon.sprintbootbankapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = {NotificationFactory.class, SmsService.class, EmailService.class})
class NotificationFactoryTest {

    @MockBean
    public SmsService smsService;

    @MockBean
    public EmailService emailService;

    @Autowired
    public NotificationFactory subject;

    @Test
    void getPreferredService() {
        assertThat(subject.getPreferredService("sms"), is(Optional.of(smsService)));
        assertThat(subject.getPreferredService("email"), is(Optional.of(emailService)));
        assertThat(subject.getPreferredService("unknown"), is(Optional.empty()));
    }

    @Test
    void notificationFactory_defaultService_returnsEmailService() {
        assertThat(subject.getDefaultNotification(), is(emailService));
    }
}