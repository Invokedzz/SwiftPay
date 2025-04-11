package org.swiftpay.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.hashids.Hashids;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.swiftpay.exceptions.InvalidEmailFormatException;
import org.swiftpay.model.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService (JavaMailSender mailSender) {

        this.mailSender = mailSender;

    }

    public void setupConfirmationEmailLogic (User user) {

        Hashids hashids = new Hashids("SHA-256");

        String token = hashids.encode(user.getId());

        createConfirmationEmailThenSend(user.getEmail(), token);

    }

    public void setupDeletionEmailLogic (String email) {

        createDeletionEmailThenSend(email);

    }

    private void createConfirmationEmailThenSend (String to, String token) {

        MimeMessage message = mailSender.createMimeMessage();

        try {

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);

            helper.setSubject("Welcome to SwiftPay – Confirm Your Account");

            var inputStream = Objects.requireNonNull(MailService.class.getResourceAsStream("/templates/confirmation-email.html"));

            String emailContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            emailContent = emailContent.replace("TOKEN_HERE", token);

            helper.setText(emailContent, true);

            mailSender.send(message);

        } catch (IOException | MessagingException ex) {

            throw new InvalidEmailFormatException("Something went wrong while rendering. Please try again.");

        }

    }

    private void createDeletionEmailThenSend (String to) {


        MimeMessage message = mailSender.createMimeMessage();

        try {

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);

            helper.setSubject("Account Deletion Confirmation");

            var inputStream = Objects.requireNonNull(MailService.class.getResourceAsStream("/templates/deletion-email.html"));

            String emailContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            helper.setText(emailContent, true);

            mailSender.send(message);

        } catch (IOException | MessagingException ex) {

            throw new InvalidEmailFormatException("Something went wrong while rendering. Please try again.");

        }

    }

}
