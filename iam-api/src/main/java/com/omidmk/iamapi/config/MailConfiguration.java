package com.omidmk.iamapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class MailConfiguration {
    @Value("${app.iam-aas.mail.from}")
    private String from;

    @Bean("CustomerCredentialsMailMessage")
    public SimpleMailMessage simpleMailMessage() {
        var message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject("Keycloak Credentials");
        message.setText("""
                Your keycloak realm credentials are:
                
                username: %s
                password: %s
                
                You can access your keycloak realm with following link: %s
                
                You must change your password after login.
                """
        );
        return message;
    }
}
