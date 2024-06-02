package lk.ijse.HelloShoesBE.service.impl;

import lk.ijse.HelloShoesBE.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceIMPL implements MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendBirthdayEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}