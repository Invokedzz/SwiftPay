package org.swiftpay.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.swiftpay.exceptions.InvalidEmailFormatException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService (JavaMailSender mailSender) {

        this.mailSender = mailSender;

    }

    public void createEmailThenSend(String to, String token) {

        MimeMessage message = mailSender.createMimeMessage();

        try {

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Welcome to SwiftPay – Confirm Your Account");

            var inputStream = Objects.requireNonNull(MailService.class.getResourceAsStream("/templates/email.html"));
            String emailContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            emailContent = emailContent.replace("TOKEN_HERE", token);

            helper.setText(emailContent, true);

            mailSender.send(message);

        } catch (IOException | MessagingException ex) {

            throw new InvalidEmailFormatException("Something went wrong while rendering. Please try again.");

        }

    }

}
