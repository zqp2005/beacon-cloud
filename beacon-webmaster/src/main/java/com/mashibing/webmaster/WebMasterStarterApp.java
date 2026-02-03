package com.mashibing.webmaster;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.Date;

/**
 * @author zjw
 * @description
 */
@SpringBootApplication
@MapperScan(basePackages = "com.mashibing.webmaster.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class WebMasterStarterApp {

    public static void main(String[] args) {
        SpringApplication.run(WebMasterStarterApp.class, args);
    }

}
