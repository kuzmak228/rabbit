package com.kuzmak.rabbit.events.listeners;

import com.kuzmak.rabbit.model.TaskMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskEventListener {

    private final RabbitTemplate rabbitTemplate;

    @EventListener
    public void publishTaskToRabbit(final TaskMessage taskMessage) {
        rabbitTemplate
                .convertSendAndReceiveAsType(
                        taskMessage.getName() + ".exchange-topic",
                        taskMessage.getName() + ".routing-key",
                        taskMessage,
                        ParameterizedTypeReference.forType(TaskMessage.class)
                );
    }
}
