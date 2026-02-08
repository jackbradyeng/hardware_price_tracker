package com.price_tracker.controllers;

import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
public class HelloWorldController {

    // hello world end point
    @GetMapping(path = "/hello")
    public String helloWorld() {
        return "Hello World.";
    }
}
