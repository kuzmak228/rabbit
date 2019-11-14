package com.kuzmak.rabbit.configuratuion;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SmartMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import javax.annotation.PreDestroy;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfiguration {

    private final RabbitProperties rabbitProperties;

    @Value("${exchange.topic:#{null}}")
    private String topicExchange;

    @Bean
    SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(
            final SimpleRabbitListenerContainerFactoryConfigurer configurer,
            final CachingConnectionFactory connectionFactory,
            final SmartMessageConverter messageConverter,
            final RetryTemplate retryTemplate
    ) {
        final var factory = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setDefaultRequeueRejected(false);
        factory.setMissingQueuesFatal(false);
        factory.setRetryTemplate(retryTemplate);

        configurer.configure(factory, connectionFactory);

        return factory;
    }


    @Bean
    @Primary
    CachingConnectionFactory cachingConnectionFactory() {
        final var cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setPassword(rabbitProperties.getPassword());
        cachingConnectionFactory.setUsername(rabbitProperties.getUsername());
        cachingConnectionFactory.setPort(rabbitProperties.getPort());
        cachingConnectionFactory.setHost(rabbitProperties.getHost());
        cachingConnectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);

        return cachingConnectionFactory;
    }


    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        final var rabbitAdmin = new RabbitAdmin(connectionFactory);
        final var queues = rabbitProperties.getQueues();
        createQueues(queues, rabbitAdmin);
        return rabbitAdmin;
    }

    @Bean
    RetryTemplate retryTemplate(final RetryPolicy retryPolicy,
                                final BackOffPolicy backOffPolicy) {
        final var retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }

    @Bean
    BackOffPolicy backOffPolicy() {
        final var backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(10000);
        return backOffPolicy;
    }

    @Bean
    RetryPolicy retryPolicy() {
        final var simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(20);
        return simpleRetryPolicy;
    }

    @Bean
    SmartMessageConverter messageConverter(final ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    private void createQueues(List<String> queues, RabbitAdmin rabbitAdmin) {
        if (isEmpty(queues)) return;
        queues.forEach(queueName -> {
            final var queue = new Queue(queueName, false, false, true, null);
            rabbitAdmin.declareQueue(queue);
            rabbitAdmin.declareExchange(new TopicExchange(queueName + "." + topicExchange));

            rabbitAdmin.declareBinding(
                    BindingBuilder
                            .bind(queue)
                            .to(new TopicExchange(queueName + "." + topicExchange))
                            .with(queueName + "." + rabbitProperties.getRoutingKey())
            );

        });
    }
}
