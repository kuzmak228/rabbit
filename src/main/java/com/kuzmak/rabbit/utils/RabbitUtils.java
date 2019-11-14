package com.kuzmak.rabbit.utils;


import com.rabbitmq.http.client.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Objects;


@Slf4j
public class RabbitUtils {

    @Value("{spring.rabbitmq.host}")
    private static String host;

    @Value("{spring.rabbitmq.host}")
    private static String port;

    @Value("{spring.rabbitmq.host}")
    private static String password;

    @Value("{spring.rabbitmq.host}")
    private static String username;


    public static Client initializeClient() {
        try {
            return new Client("http://" + host + ":" + port + "/api/",
                    Objects.requireNonNull(password, "Password cannot be null"),
                    Objects.requireNonNull(username, "Username cannot be null"));
        } catch (MalformedURLException | URISyntaxException e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }
}
