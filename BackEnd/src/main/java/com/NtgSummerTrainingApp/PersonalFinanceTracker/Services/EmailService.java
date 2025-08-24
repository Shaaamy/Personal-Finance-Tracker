package com.NtgSummerTrainingApp.PersonalFinanceTracker.Services;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@RequiredArgsConstructor
@Service
public class EmailService{

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Password Reset Request");
            String resetLink = "http://your-frontend/reset-password?token=" + token;
            helper.setText("Click the link to reset your password: " + resetLink, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}
