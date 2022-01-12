package com.sp.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@ComponentScan(basePackages = "com.sp.domain")
@SpringBootApplication(scanBasePackages = "com.sp")
public class StringcatApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StringcatApiApplication.class, args);
    }

}
