package com.smagin.demoapplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/ping")
    public String pong() {
        return "pong";
    }
}
