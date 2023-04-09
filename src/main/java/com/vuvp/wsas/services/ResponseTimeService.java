package com.vuvp.wsas.services;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class ResponseTimeService {

    private final RestTemplate restTemplate;

    public ResponseTimeService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = new RestTemplate();
    }

    public long getResponseTime(String url, String method){
        long startTime;
        if(Objects.equals(method, "GET")){
            startTime = System.currentTimeMillis();
            restTemplate.getForObject(url, String.class);
        }
        else {
            //startTime = System.currentTimeMillis();
            //restTemplate.postForObject(url, "", getClass());
            return -1;
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
