package com.sp.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class HealthController {

    @GetMapping("/")
    public String root() {
        return "ok";
    }

    @GetMapping("/health")
    public String health() {
        return "ok";
    }
}
