package com.kuzmak.rabbit.rabbitConfig;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Listener {


  /*  @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(value = "queue"),
                    exchange = @Exchange(value = "${exchange.topic}", type = ExchangeTypes.TOPIC))})
    protected void receiveMessage(String message) {
        System.out.println(message);
    }*/

}
