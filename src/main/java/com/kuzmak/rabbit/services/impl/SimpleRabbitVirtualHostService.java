package com.kuzmak.rabbit.services.impl;

import com.kuzmak.rabbit.exceptions.InvalidQueueDeclarationException;
import com.kuzmak.rabbit.services.RabbitVirtualHostService;
import com.kuzmak.rabbit.utils.RabbitUtils;
import com.rabbitmq.http.client.Client;
import com.rabbitmq.http.client.domain.QueueInfo;
import com.rabbitmq.http.client.domain.VhostInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Service
@Slf4j
public class SimpleRabbitVirtualHostService implements RabbitVirtualHostService {

    private Client client;

    @PostConstruct
    private void init() {
        client = RabbitUtils.initializeClient();
    }

    @Override
    public List<VhostInfo> getVirtualHostInformation() {
        log.info("Trying to get existing virtual hosts");
        return client.getVhosts();
    }

    @Override
    public void createVirtualHost(final String name) {
        log.info("Creating virtual host with name {}", name);
        client.createVhost(name);
    }

}
