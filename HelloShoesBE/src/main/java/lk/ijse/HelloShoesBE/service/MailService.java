package lk.ijse.HelloShoesBE.service;

public interface MailService {
    void sendBirthdayEmail(String to, String subject, String text);
}