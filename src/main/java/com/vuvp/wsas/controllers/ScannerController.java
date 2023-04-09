package com.vuvp.wsas.controllers;

import com.vuvp.wsas.models.LoadTestRequest;
import com.vuvp.wsas.services.LoadTestResultExtractionService;
import com.vuvp.wsas.services.LoadTestingService;
import com.vuvp.wsas.services.ResponseTimeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/scanner")
public class ScannerController {

    private final ResponseTimeService responseTimeService;
    private final LoadTestingService loadTestingService;
    private final LoadTestResultExtractionService loadTestResultExtractionService;

    @Value("${base.url}")
    private String baseUrl;

    public ScannerController(ResponseTimeService responseTimeService,
                             LoadTestingService loadTestingService,
                             LoadTestResultExtractionService loadTestResultExtractionService) {
        this.responseTimeService = responseTimeService;
        this.loadTestingService = loadTestingService;
        this.loadTestResultExtractionService = loadTestResultExtractionService;
    }

    // Main view
    @GetMapping("/")
    public String myView(Model model) {
        System.out.println(baseUrl);
        model.addAttribute("baseUrl", baseUrl);
        System.out.println(model.getAttribute("baseUrl"));
        return "index";
    }

    // Get The Response Time
    // Send a simple Get/Post Request by the url parameter and measure the response time.
    @PostMapping("/getResponseTime")
    @ResponseBody
    public long getResponseTime(@RequestBody LoadTestRequest loadTestRequest)
            throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return responseTimeService.getResponseTime(loadTestRequest.getUrl(), loadTestRequest.getRequestMethod());
    }

    // SQL Injection
    // Soon... (As far as I know it is illegal to do those attacks on sites,
    // and I don't have an appropriate web app on my machine so...)

    // HTTPS/HTTP
    @PostMapping("/scanUrlForProtocol")
    @ResponseBody
    public String scanUrlForProtocol(@RequestBody LoadTestRequest loadTestRequest) {
        try {
            var uri = new URI(loadTestRequest.getUrl());
            return uri.getScheme();
        } catch (URISyntaxException e) {
            return "Invalid URI: " + e.getMessage();
        }
    }

    // Task:
    // 10 users are sending the same requests concurrently
    // All the users are loaded not at the same time(all of them are loaded after 5 seconds)
    // Users(Threads) remain the same(they do not change on each loop iteration)
    // Each one of them sends a request five times in a row

    // Load Testing
    @PostMapping("/scanLocalhostForPerformance")
    @ResponseBody
    // String url, String requestMethod, int users, int timeToLoadAllUsers, int loopCount
    public String scanLocalhostForPerformance(@RequestBody LoadTestRequest loadTestRequest) throws Exception{
        var fullDirectoryName = loadTestingService.runLoadTest(
                loadTestRequest.getUrl(),
                loadTestRequest.getRequestMethod(),
                loadTestRequest.getUsers(),
                loadTestRequest.getTimeToLoadAllUsers(),
                loadTestRequest.getLoopCount()
        );

        return loadTestResultExtractionService.getAverageResponseTime(fullDirectoryName) + "ms";
    }
}