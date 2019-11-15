package com.kuzmak.rabbit.services;

import com.rabbitmq.http.client.domain.VhostInfo;

import java.util.List;

public interface RabbitVirtualHostService {

    List<VhostInfo> getVirtualHostInformation();

    void createVirtualHost(String name);

    void createVirtualHosts(List<String> names);

    String getOrCreateHost(String name);
}
