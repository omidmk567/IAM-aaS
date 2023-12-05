package com.omidmk.iamapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IAMApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(IAMApiApplication.class, args);
    }

}
