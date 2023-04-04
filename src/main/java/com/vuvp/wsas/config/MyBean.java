package com.vuvp.wsas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyBean {
    @Value("${server.port}")
    private int serverPort;

    // other code
}
