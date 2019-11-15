package com.kuzmak.rabbit.events.publishers;

import com.kuzmak.rabbit.model.TaskMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultTaskEventPublisher implements TaskEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishTask(final TaskMessage taskMessage) {
        eventPublisher.publishEvent(taskMessage);
    }
}
