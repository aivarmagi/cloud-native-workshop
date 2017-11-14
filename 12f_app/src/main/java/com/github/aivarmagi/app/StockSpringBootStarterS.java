
package com.github.aivarmagi.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@SpringBootApplication
public class StockSpringBootStarterS {
    public static void main(String[] args) {
        SpringApplication.run(StockSpringBootStarterS.class, args);
    }
}

