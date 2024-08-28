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

    @PostMapping
    public String sendMail(@RequestBody String mail) {
        log.info(mail);
        boolean sentMail = mailService.sendSimpleMail(mail);
        return sentMail ? "Success" : "Failure";
    }
}
