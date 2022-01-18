package com.sp.domain.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfig {

    @Bean
    public DataSource datasource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://heejeong-database.cv35wjiwae1x.ap-northeast-2.rds.amazonaws.com:3306/stringcat")
                .username("heejeong")
                .password("Stringcat-db")
                .build();
    }
}