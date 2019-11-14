package com.kuzmak.rabbit.events;

import com.kuzmak.rabbit.configuratuion.RabbitProperties;
import com.kuzmak.rabbit.services.RabbitVirtualHostService;
import com.rabbitmq.http.client.domain.VhostInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static com.google.common.collect.Iterables.tryFind;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitApplicationReadyEvent implements ApplicationListener<ApplicationReadyEvent> {

    private final RabbitProperties rabbitProperties;
    private final RabbitVirtualHostService virtualHostService;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        final var virtualHostInformation = virtualHostService.getVirtualHostInformation();

        if (!virtualHostExists(virtualHostInformation, rabbitProperties.getVirtualHost())) {
            virtualHostService.createVirtualHost(rabbitProperties.getVirtualHost());
        }

    }

    private boolean virtualHostExists(final List<VhostInfo> virtualHostInformation, final String virtualHost) {
        return !isEmpty(virtualHostInformation) && tryFind(virtualHostInformation, vh -> vh != null && Objects.equals(vh.getName(), virtualHost)).isPresent();

    }
}
