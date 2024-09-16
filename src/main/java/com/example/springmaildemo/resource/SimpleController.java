package com.example.springmaildemo.resource;

import com.example.springmaildemo.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SimpleController {

    private final MailService mailService;

    @GetMapping
    public String hello() {
        log.info("hello");
        return "hello";
    }

    /**
     * Send a simple mail with the given recipient's email address.
     * The email's subject is "Test Mail Subject" and the content is "Hi,\nTest Mail Content.\nThanks".
     * @param mail the recipient's email address
     * @return "Success" if the mail is sent successfully, "Failure" otherwise.
     */
    @PostMapping
    public String sendMail(@RequestBody String mail) {
        log.info(mail);
        boolean sentMail = mailService.sendSimpleMail(mail);
        return sentMail ? "Success" : "Failure";
    }

    @PostMapping("/mail")
    public String sendMailWithApi(@RequestBody String mail) {
        log.info(mail);
        boolean sentMail = mailService.sendMimeMail(mail);
        return sentMail ? "Success" : "Failure";
    }
}
