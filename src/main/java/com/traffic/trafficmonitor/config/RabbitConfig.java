package com.traffic.trafficmonitor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class RabbitConfig {


    @Value("${rabbitmq.queueNonDurable}")
    private boolean isNonDurable;

    @Value("#{${rabbitmq.queues.map}}")
    private Map<String, String> queuesMap;

    @Value("${rabbitmq.topicExchangeName}")
    private String topicExchangeName;

    @Value("${rabbitmq.prefetchCount}")
    private int prefetchCount;

    @Bean
    public Declarables topicBindings() {

        // create Declarable list
        List<Declarable> declarables = new ArrayList<Declarable>();

        // create exchange
        Declarable exchange = new TopicExchange(topicExchangeName, isNonDurable, false);

        List<Declarable> bindings = new ArrayList<>();

        for (String key: queuesMap.keySet()) {
            Declarable declarable = new Queue(key, isNonDurable);
            declarables.add(declarable);
            bindings.add(
                    BindingBuilder
                            .bind((Queue)declarable)
                            .to((TopicExchange)exchange)
                            .with(key));
        }

        declarables.add(exchange);
        declarables.addAll(bindings);

        return new Declarables(declarables);
    }

    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> rabbitListenerContainerFactory(ConnectionFactory rabbitConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(rabbitConnectionFactory);
        factory.setPrefetchCount(prefetchCount);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}


