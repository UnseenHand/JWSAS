package com.vuvp.wsas.controllers;

import com.vuvp.wsas.services.ResponseTimeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/scanner")
public class ScannerController {

    private final ResponseTimeService responseTimeService;

    public ScannerController(ResponseTimeService responseTimeService) {
        this.responseTimeService = responseTimeService;
    }

    //Get Response Time
    //Send a simple Get Request by the url parameter and measure the response time.
    @GetMapping("/scanForGetResponseTime")
    @ResponseBody
    public long scanForGetResponseTime(String url) {
        return responseTimeService.getResponseTime(url);
    }


    //SQL Injection


    //HTTPS/HTTP


}