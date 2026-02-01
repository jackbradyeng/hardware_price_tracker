package com.price_tracker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PriceTrackerController {

    // end point
    @GetMapping(path = "/hello")
    public String helloWorld() {
        return "Hello World.";
    }
}
