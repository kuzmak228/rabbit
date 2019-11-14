package com.kuzmak.rabbit.events;

import com.kuzmak.rabbit.exceptions.InvalidQueueDeclarationException;
import com.kuzmak.rabbit.services.RabbitQueueService;
import com.kuzmak.rabbit.services.RabbitVirtualHostService;
import com.rabbitmq.http.client.domain.VhostInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.google.common.collect.Iterables.tryFind;
import static org.apache.commons.collections4.CollectionUtils.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitApplicationReadyEvent implements ApplicationListener<ApplicationReadyEvent> {


    private final RabbitVirtualHostService virtualHostService;
    private final RabbitQueueService queueService;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        final var virtualHostInformation = virtualHostService.getVirtualHostInformation();

        if(!virtualHostExists(virtualHostInformation, "dev")) {
            virtualHostService.createVirtualHost("dev");
        }

        try {
            if (queueService.queueExists("/", "queue")) {
                queueService.declareQueue("/", "queue", false, false, true);
            }
        } catch (final InvalidQueueDeclarationException e) {
            log.error(e.getMessage(), e);
        }

    }

    private boolean virtualHostExists(final List<VhostInfo> virtualHostInformation, final String virtualHost) {
        return !isEmpty(virtualHostInformation) && tryFind(virtualHostInformation, vh -> vh != null && Objects.equals(vh.getName(), virtualHost)).isPresent();

    }
}
