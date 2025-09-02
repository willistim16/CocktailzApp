package com.cocktailz.cocktailzclean.controller;

import com.cocktailz.cocktailzclean.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailTestController {

    private final EmailService emailService;

    public EmailTestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/api/test-email")
    public String testEmail(@RequestParam String to) {
        emailService.sendEmail(to, "Test Email from Cocktailz", "Dit is een test email!");
        return "Test email sent to " + to;
    }
}
