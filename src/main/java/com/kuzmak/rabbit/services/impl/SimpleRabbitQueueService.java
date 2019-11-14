package com.kuzmak.rabbit.services.impl;

import com.kuzmak.rabbit.exceptions.InvalidQueueDeclarationException;
import com.kuzmak.rabbit.services.RabbitQueueService;
import com.kuzmak.rabbit.utils.RabbitUtils;
import com.rabbitmq.http.client.Client;
import com.rabbitmq.http.client.domain.QueueInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Service
@Slf4j
public class SimpleRabbitQueueService implements RabbitQueueService {

    private Client client;

    @PostConstruct
    private void init() {
        client = RabbitUtils.initializeClient();
    }

    @Override
    public void declareQueue(final String vhost, final String name, final boolean durable, final boolean exclusive, final boolean autoDelete) {
        log.info("Creating queue {} on virtual host {}", name, vhost);
        client.declareQueue(vhost, name, new QueueInfo(durable, exclusive, autoDelete));
    }

    @Override
    public QueueInfo getQueue(final String vhost, final String name) throws InvalidQueueDeclarationException {
        if (isEmpty(vhost) || isEmpty(name))
            throw new InvalidQueueDeclarationException("Virtual host or queue name is empty");

        return client.getQueue(vhost, name);
    }

    @Override
    public boolean queueExists(final String vhost, final String name) throws InvalidQueueDeclarationException {
        return getQueue(vhost, name) != null;
    }

    @Override
    public List<QueueInfo> getAllQueues() {
        log.info("Trying to get all queues");
        return client.getQueues();
    }
}
