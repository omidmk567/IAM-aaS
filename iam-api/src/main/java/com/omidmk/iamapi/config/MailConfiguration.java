package com.omidmk.iamapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class MailConfiguration {
    @Bean("CustomerCredentialsMailMessage")
    public SimpleMailMessage simpleMailMessage() {
        var message = new SimpleMailMessage();
        message.setFrom("noreply@IAM.aas.project");
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
