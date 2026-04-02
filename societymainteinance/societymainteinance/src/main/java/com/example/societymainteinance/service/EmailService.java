package com.example.societymainteinance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Vendor Login OTP");
        message.setText(
                "🔐 Vendor Login OTP\n\n" +
                "Your one-time password (OTP) is:\n\n" +
                "👉 " + otp + " 👈\n\n" +
                "⏳ This OTP is valid for *5 minutes*.\n" +
                "Please do not share it with anyone for security reasons 🚫\n\n" +
                "If you did not request this OTP, please ignore this message.\n\n" +
                "Regards,\n" +
                "🏢 Society Management Team"
        );

        mailSender.send(message);
    }
    
    private void sendEmail(String toEmail, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
    
    public void sendComplaintResolvedEmail(String email, String name, Long complaintId) {
        String subject = "✅ Your complaint has been resolved";
        String body = "Hello " + name + ",\n\n" +
                "Your complaint (ID: " + complaintId + ") has been successfully resolved.\n\n" +
                "Thank you for your patience.\n\n" +
                "Society Management Team";

        sendEmail(email, subject, body);
    }

    
    public void sendWelcomeEmail(String toEmail, String name) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome to Society Maintenance System 🎉");

        message.setText(
                "Hello " + name + " 👋,\n\n" +
                "🎉 Welcome to the Society Maintenance System! 🎉\n\n" +
                "Your registration was successful, and you’re officially part of our community 🏡✨\n\n" +
                "You can now log in to:\n" +
                "🔧 Raise and track complaints\n" +
                "📢 View society announcements\n" +
                "💳 Stay updated with maintenance and services\n\n" +
                "We’re excited to have you with us and look forward to making your stay comfortable and hassle-free 😊\n\n" +
                "Warm regards,\n" +
                "🏢 Society Management Team"
        );

        mailSender.send(message);
    }


}
