package com.rabbit0tank.connect_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * @author liqh
 */
@ComponentScan(basePackages = {"com.rabbit0tank"})
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.rabbit0tank.connect_service")
@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
