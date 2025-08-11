package com.example.in_sky1;

import com.example.in_sky1.ws.WebSocketServer;
import jakarta.annotation.PostConstruct;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@ServletComponentScan
@SpringBootApplication
@EnableCaching
public class InSky1Application implements CommandLineRunner {
    @Autowired
    private WebSocketServer webSocketServer;

    public static void main(String[] args) {
        SpringApplication.run(InSky1Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 应用启动完成后再启动Netty服务
        webSocketServer.setPort(8082).start();
    }
}
