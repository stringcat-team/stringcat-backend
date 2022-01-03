package com.sp.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sp")
public class StringcatApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StringcatApiApplication.class, args);
    }

}
