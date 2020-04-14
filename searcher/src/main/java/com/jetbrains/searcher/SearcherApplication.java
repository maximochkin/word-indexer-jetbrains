package com.jetbrains.searcher;

import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class SearcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearcherApplication.class, args);
    }

    @Bean
    Logger.Level getFeignLoggerLevel() {
        return Logger.Level.FULL;
    }

}
