package com.vuvp.wsas.services;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ResponseTimeService {

    private RestTemplate restTemplate;

    public ResponseTimeService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public long getResponseTime(String url) {
        long startTime = System.currentTimeMillis();
        restTemplate.getForObject(url, String.class);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
