package com.mashibing.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 郑大仙丶
 * @version V1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MonitorStarterApp {

    public static void main(String[] args) {
        SpringApplication.run(MonitorStarterApp.class,args);
    }

}
