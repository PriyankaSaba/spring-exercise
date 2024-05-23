package com.example.companysearch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(apiKeyInterceptor()));
        return restTemplate;
    }

    private ClientHttpRequestInterceptor apiKeyInterceptor() {
        return (request, body, execution) -> {
            request.getHeaders().add("x-api-key", "your-api-key-here");
            return execution.execute(request, body);
        };
    }
}

