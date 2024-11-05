package com.rabbit0tank.mybatis_project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.rabbit0tank"})
@SpringBootApplication
@Slf4j
public class MybatisProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisProjectApplication.class, args);
    }

}
