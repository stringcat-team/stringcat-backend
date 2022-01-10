package com.sp.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sp")
public class StringcatBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(StringcatBatchApplication.class, args);
    }

}