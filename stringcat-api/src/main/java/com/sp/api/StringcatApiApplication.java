package com.sp.api;

import com.sp.api.common.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = "com.sp")
@EnableConfigurationProperties(AppProperties.class)
public class StringcatApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StringcatApiApplication.class, args);
    }

}
