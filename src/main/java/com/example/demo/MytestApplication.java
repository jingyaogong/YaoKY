package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MytestApplication extends SpringBootServletInitializer {
    //public class MytestApplication {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MytestApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MytestApplication.class, args);
    }

}




























