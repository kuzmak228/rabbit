package com.kuzmak.rabbit.utils;


import com.rabbitmq.http.client.Client;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Objects;


@Slf4j
public class RabbitUtils {

    public static Client initializeClient() {
        try {
            return new Client("http://" + "localhost" + ":" + "15672" + "/api/", Objects.requireNonNull("guest"), Objects.requireNonNull("guest"));
        } catch (MalformedURLException | URISyntaxException e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }
}
