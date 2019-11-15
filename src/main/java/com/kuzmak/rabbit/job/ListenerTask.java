package com.kuzmak.rabbit.job;

import com.kuzmak.rabbit.events.publishers.TaskEventPublisher;
import com.kuzmak.rabbit.model.TaskMessage;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ListenerTask extends DefaultTask {

    public ListenerTask(final TaskEventPublisher eventPublisher) {
        super(eventPublisher);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(value = "ListenerTask"), key = "ListenerTask.routing-key",
                    exchange = @Exchange(value = "ListenerTask.exchange-topic", type = ExchangeTypes.TOPIC))})
    protected void receiveMessage(final TaskMessage message) {
        super.receiveMessage(message);
    }


   /* @Override
    @Async
    @Scheduled(initialDelay = 60 * 100, fixedDelay = 60 * 100)
    protected void schedule() {
        super.schedule();
    }*/

    @Override
    protected boolean execute(final TaskMessage message) {
        return true;
    }

    @Override
    protected String getTaskName() {
        return getClass().getSimpleName();
    }
}
