package com.example.todo.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamportConfig {

    @Value("${iamport.key}")
    private String key;
    @Value("${iamport.secret}")
    private String secret;
    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(key, secret);
    }
}