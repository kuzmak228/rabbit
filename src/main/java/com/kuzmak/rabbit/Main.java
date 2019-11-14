package com.kuzmak.rabbit;

import com.kuzmak.rabbit.configuratuion.RabbitProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PreDestroy;

@SpringBootApplication
@EnableConfigurationProperties
@RequiredArgsConstructor
public class Main {

    private final RabbitProperties rabbitProperties;
    private final RabbitAdmin admin;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @PreDestroy
    public void destroy() {
        rabbitProperties.getQueues().forEach(admin::deleteQueue);
    }


}
