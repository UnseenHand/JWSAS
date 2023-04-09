package com.vuvp.wsas.services;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Service
public class ResponseTimeService {

    private final RestTemplate restTemplate;

    public ResponseTimeService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = new RestTemplate();
    }

    public long getResponseTime(String url, String method)
            throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        long startTime;
        CloseableHttpClient client;
        if(Objects.equals(method, "GET")){
            SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
            sslContextBuilder.loadTrustMaterial(null, (certificate, authType) -> true);
            SSLConnectionSocketFactory sslConnectionSocketFactory =
                    new SSLConnectionSocketFactory(sslContextBuilder.build());
            client = HttpClients.custom()
                    .setSSLSocketFactory(sslConnectionSocketFactory)
                    .build();
            HttpGet request = new HttpGet(url);
            startTime = System.currentTimeMillis();
            client.execute(request);
        }
        else {
            //startTime = System.currentTimeMillis();
            //restTemplate.postForObject(url, "", getClass());
            return -1;
        }
        long endTime = System.currentTimeMillis();
        client.close();
        return endTime - startTime;
    }
}
