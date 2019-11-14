package com.kuzmak.rabbit.rabbitConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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

@Configuration
public class RabbitMQConfiguration {

    @Value("${exchange.topic:#{null}}")
    private String topicExchange;

    @Bean
    SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(
            final SimpleRabbitListenerContainerFactoryConfigurer configurer,
            final CachingConnectionFactory connectionFactory,
            final Jackson2JsonMessageConverter messageConverter,
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
        cachingConnectionFactory.setPassword("guest");
        cachingConnectionFactory.setUsername("guest");
        cachingConnectionFactory.setPort(15672);
        cachingConnectionFactory.setHost("localhost");

        return cachingConnectionFactory;
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
    Jackson2JsonMessageConverter messageConverter(final ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }
}
