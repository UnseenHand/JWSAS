package com.vuvp.wsas.controllers;

import com.vuvp.wsas.exceptions.JMeterConfigurationException;
import com.vuvp.wsas.services.LoadTestingService;
import com.vuvp.wsas.services.ResponseTimeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/scanner")
public class ScannerController {

    private final ResponseTimeService responseTimeService;
    private final LoadTestingService loadTestingService;

    public ScannerController(ResponseTimeService responseTimeService, LoadTestingService loadTestingService) {
        this.responseTimeService = responseTimeService;
        this.loadTestingService = loadTestingService;
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



    //Task: 5 user response time in parallel compared to 1 use response time

    //Load Testing
    @GetMapping("/scanLocalhostForPerformance")
    @ResponseBody
    public String scanLocalhostForPerformance() {
        try{
            loadTestingService.runLoadTest();
            return "Success";
        }catch (JMeterConfigurationException e) {
            return e.getMessage();
        }
    }


}