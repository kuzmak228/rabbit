package com.kuzmak.rabbit.services;

import com.kuzmak.rabbit.exceptions.InvalidQueueDeclarationException;
import com.rabbitmq.http.client.domain.QueueInfo;

public interface RabbitQueueService {

    void declareQueue(String vhost, String name, boolean durable, boolean exclusive, boolean autoDelete);

    QueueInfo getQueue(String vhost, String name) throws InvalidQueueDeclarationException;

    boolean queueExists(String vhost, String name) throws InvalidQueueDeclarationException;
}
