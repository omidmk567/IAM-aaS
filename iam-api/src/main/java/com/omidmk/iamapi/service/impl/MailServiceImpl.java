package com.omidmk.iamapi.service.impl;

import com.omidmk.iamapi.exception.SendingMailFailedException;
import com.omidmk.iamapi.service.MailService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final SimpleMailMessage mailMessage;

    public MailServiceImpl(JavaMailSender mailSender, @Qualifier("CustomerCredentialsMailMessage") SimpleMailMessage mailMessage) {
        this.mailSender = mailSender;
        this.mailMessage = mailMessage;
    }

    @Override
    public void sendCustomerCredentials(String mail, String username, String password, String realmUrl) throws SendingMailFailedException {
        var message = new SimpleMailMessage(mailMessage);
        message.setTo(mail);
        message.setText(Objects.requireNonNull(mailMessage.getText()).formatted(username, password, realmUrl));
        try {
            mailSender.send(message);
        } catch (MailException ex) {
            throw new SendingMailFailedException("Failed to send mail to %s. %s".formatted(mail, ex.getMessage()));
        }
    }
}
