package com.example.springmaildemo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

@Slf4j
@Service
public class MailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String FROM_MAIL;


    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    //    @Async
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


    /**
     * Sends a mime mail to the given recipient's email address.
     * The email's subject is "Test Mail Subject" and the content is an HTML
     * text with a heading and a paragraph. The email also has an attachment
     * named "test.txt".
     *
     * @param mail the recipient's email address
     * @return true if the mail is sent successfully, false otherwise
     */
    public boolean sendMimeMail(String mail) {

        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("Spring <" + FROM_MAIL + ">");
            helper.addTo(mail);
//            helper.addCc(mail);
            helper.setSubject("Test Mail Subject");
            helper.setText("<html><body><h1>Hi, Test Mail Content.</h1><p>Thanks</p></body></html>", true);

            FileUrlResource fileUrlResource = new FileUrlResource("/home/arun/Pictures/mai-1.png");
            helper.addAttachment(fileUrlResource.getFilename(), fileUrlResource);

            mailSender.send(mimeMessage);

        } catch (MessagingException | MalformedURLException e) {
            log.error("Failed to send mail to {} due to {}", mail, e.getMessage());
            throw new RuntimeException(e);
        }

        return true;
    }


    @Recover
    public boolean recover(MailSendException e) {
        log.info("recover : Failed to send mail due to {}", e.getMessage());
        return false;
    }

}
