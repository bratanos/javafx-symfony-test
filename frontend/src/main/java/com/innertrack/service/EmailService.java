package com.innertrack.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailService {

    private static EmailService instance;
    private final Properties props = new Properties();

    private EmailService() {
        loadConfig();
    }

    public static synchronized EmailService getInstance() {
        if (instance == null) {
            instance = new EmailService();
        }
        return instance;
    }

    private void loadConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("Sorry, unable to find config.properties");
                return;
            }
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendVerificationEmail(String recipientEmail, String otpCode) {
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        props.getProperty("mail.smtp.username"),
                        props.getProperty("mail.smtp.password"));
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(props.getProperty("mail.from")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("InnerTrack - Votre code de vérification");

            String htmlContent = "<html><body style='font-family: Arial, sans-serif; color: #1e293b;'>" +
                    "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e2e8f0; border-radius: 12px;'>"
                    +
                    "<h2 style='color: #7c3aed;'>InnerTrack</h2>" +
                    "<p>Bonjour,</p>" +
                    "<p>Merci de vous être inscrit sur InnerTrack. Voici votre code de vérification :</p>" +
                    "<div style='font-size: 32px; font-weight: bold; color: #7c3aed; letter-spacing: 5px; text-align: center; padding: 20px; background-color: #f5f3ff; border-radius: 8px; margin: 20px 0;'>"
                    +
                    otpCode + "</div>" +
                    "<p>Ce code expirera dans 10 minutes. Si vous n'êtes pas à l'origine de cette demande, vous pouvez ignorer cet email.</p>"
                    +
                    "<hr style='border: 0; border-top: 1px solid #e2e8f0; margin: 20px 0;'>" +
                    "<p style='font-size: 12px; color: #64748b; text-align: center;'>© 2026 InnerTrack — Tous droits réservés.</p>"
                    +
                    "</div></body></html>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("DEBUG: Email legitimately sent to " + recipientEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("ERROR: Failed to send email to " + recipientEmail);
        }
    }

    public void sendPasswordResetEmail(String recipientEmail, String otpCode) {
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        props.getProperty("mail.smtp.username"),
                        props.getProperty("mail.smtp.password"));
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(props.getProperty("mail.from")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("InnerTrack - Réinitialisation de votre mot de passe");

            String htmlContent = "<html><body style='font-family: Arial, sans-serif; color: #1e293b;'>" +
                    "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e2e8f0; border-radius: 12px;'>"
                    +
                    "<h2 style='color: #7c3aed;'>InnerTrack</h2>" +
                    "<p>Bonjour,</p>" +
                    "<p>Vous avez demandé la réinitialisation de votre mot de passe. Voici votre code sécurisé :</p>" +
                    "<div style='font-size: 32px; font-weight: bold; color: #7c3aed; letter-spacing: 5px; text-align: center; padding: 20px; background-color: #f5f3ff; border-radius: 8px; margin: 20px 0;'>"
                    +
                    otpCode + "</div>" +
                    "<p>Ce code expirera dans 10 minutes. Si vous n'êtes pas à l'origine de cette demande, veuillez ignorer cet email.</p>"
                    +
                    "<hr style='border: 0; border-top: 1px solid #e2e8f0; margin: 20px 0;'>" +
                    "<p style='font-size: 12px; color: #64748b; text-align: center;'>© 2026 InnerTrack — Tous droits réservés.</p>"
                    +
                    "</div></body></html>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("DEBUG: Reset Email legitimately sent to " + recipientEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("ERROR: Failed to send reset email to " + recipientEmail);
        }
    }
}
