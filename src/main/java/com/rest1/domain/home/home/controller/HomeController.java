package com.rest1.domain.home.home.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class HomeController {

    @GetMapping
    public String home() throws UnknownHostException {

        InetAddress localhost = InetAddress.getLocalHost();

        return """
                <h1>Welcome to Rest1</h1>
                <p>Server IP Address: %s</p>
                <p>Server Host Name: %s</p>
                <div>
                    <a href="swagger-ui/index.html">API 문서로 이동</a>
                </div>
                """.formatted(localhost.getHostAddress(), localhost.getHostName());
    }

}