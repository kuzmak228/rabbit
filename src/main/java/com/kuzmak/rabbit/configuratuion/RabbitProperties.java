package com.kuzmak.rabbit.configuratuion;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Getter
@Setter
public class RabbitProperties {

    private String host;
    private Integer port;
    private String virtualHost;
    private String username;
    private String password;
    private List<String> queues;
    private String routingKey;

}
