package com.kuzmak.rabbit.configuratuion;

import com.kuzmak.rabbit.model.Test;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class Listener {

    private final RabbitTemplate asyncRabbitTemplate;

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(value = "Listener"), key = "Listener.routing-key",
                    exchange = @Exchange(value = "Listener.exchange-topic", type = ExchangeTypes.TOPIC))})
    protected void receiveMessage(Test message) {
        System.out.println(message);
    }

    @PostConstruct()
    public void init() {
        asyncRabbitTemplate
                .convertSendAndReceiveAsType(
                        "Listener.exchange-topic",
                        "Listener.routing-key",
                        new Test("gleb", "kuzmitski"),
                        ParameterizedTypeReference.forType(Test.class)
                );
    }

}
