package com.i9developed.oauth2.ws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.i9developed.oauth2.ws.services.email.EmailService;
import com.i9developed.oauth2.ws.services.email.SmtpEmailService;

@Configuration
@PropertySource("classpath:application.properties")
public class EmailConfig {

    @Bean
    public EmailService emailService(){
        return new SmtpEmailService();
    }
}
