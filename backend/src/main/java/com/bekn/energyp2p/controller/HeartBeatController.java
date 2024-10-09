package com.bekn.energyp2p.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeartBeatController {
    @GetMapping("/health")
    public String checkHealth() {
        return "Server is running!";
    }
}
