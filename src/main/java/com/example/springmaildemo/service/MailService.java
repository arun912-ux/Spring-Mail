package com.example.springmaildemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String FROM_MAIL;

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000L, multiplier = 2.0))
    public boolean sendSimpleMail(String mail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(FROM_MAIL);
            message.setTo(mail);
            message.setSubject("Test Mail Subject");
            message.setText("Hi, \nTest Mail Content.\nThanks");
            log.info("Sending mail to {}", mail);
            mailSender.send(message);
        } catch (MailSendException e) {
            log.info("Failed to send mail to {} due to {}", mail, e.getMessage());
            throw e;
        }
        return true;
    }


    @Recover
    public boolean recover(MailSendException e) {
        log.info("recover : Failed to send mail due to {}", e.getMessage());
        return false;
    }

}
