package com.omidmk.iamapi;

import com.omidmk.iamapi.config.KeycloakProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(KeycloakProperties.class)
public class IAMApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(IAMApiApplication.class, args);
    }

}
