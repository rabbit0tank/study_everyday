package com.rabbit0tank.connect_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * @author liqh
 */
//@EnableJpaAuditing
//@EntityScan("com.rabbit0tank.connect_service.web.entity")
@Slf4j
//@EnableJpaRepositories(basePackages = "com.rabbit0tank.connect_service.web.dao") // 指定仓库包
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
