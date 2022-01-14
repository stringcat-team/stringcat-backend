package com.sp.domain.config;

import com.sp.domain.StringcatDomainApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackageClasses = StringcatDomainApplication.class)
@EnableJpaRepositories(basePackageClasses =  StringcatDomainApplication.class)
public class JpaConfig {
}
