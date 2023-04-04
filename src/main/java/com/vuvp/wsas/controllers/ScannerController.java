package com.vuvp.wsas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.*;

@Controller
public class ScannerController {

    //SQL Injection
    //HTTPS/HTTP
    //Response Time
    @GetMapping("/scan")
    @ResponseBody
    public static long scan(String url) throws URISyntaxException, IOException, InterruptedException {
        System.out.println(url);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(url))
                .headers("Accept-Enconding", "gzip, deflate")
                .build();
//        HttpResponse<String> response =
        var startTime = System.currentTimeMillis();

        client.send(request, HttpResponse.BodyHandlers.ofString());

        var endTime = System.currentTimeMillis();

        var timeTaken = endTime - startTime;
        System.out.println("Response time:" + timeTaken + "ms");
        return timeTaken;
/*        String responseBody = response.body();
        int responseStatusCode = response.statusCode();


        System.out.println("httpGetRequest: " + responseBody);
        System.out.println("httpGetRequest status code: " + responseStatusCode);*/
    }
}