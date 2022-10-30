package com.example.challengejavaspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class ChallengeJavaSpringBootApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(ChallengeJavaSpringBootApplication.class, args);
    }



}
