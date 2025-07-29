package com.example.in_sky1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@ServletComponentScan
@SpringBootApplication
@EnableCaching
public class InSky1Application {

    public static void main(String[] args) {
        SpringApplication.run(InSky1Application.class, args);
    }

}
