package com.ty_yak.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.ty_yak.auth", "com.konstde00.commons"})
@EnableJpaRepositories(basePackages = {"com.ty_yak.*"})
@SpringBootApplication(scanBasePackages = {"com.ty_yak.*"})
public class TyYakApp {

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(TyYakApp.class);
        springApplication.run(args);
    }
}
