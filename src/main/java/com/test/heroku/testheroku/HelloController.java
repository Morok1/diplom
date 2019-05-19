package com.test.heroku.testheroku;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(){
        return "Hello";
    }

    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }
}
