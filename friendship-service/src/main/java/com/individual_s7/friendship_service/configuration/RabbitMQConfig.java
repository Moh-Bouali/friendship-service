package com.individual_s7.friendship_service.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    // Friendship Request Configuration
    public static final String FRIEND_REQUEST_EXCHANGE = "friendRequestExchange";
    public static final String FRIEND_REQUEST_QUEUE = "friendRequestQueue";
    public static final String FRIEND_REQUEST_ROUTING_KEY = "friendRequestKey";

    @Bean
    public DirectExchange friendRequestExchange() {
        return new DirectExchange(FRIEND_REQUEST_EXCHANGE);
    }

    @Bean
    public Queue friendRequestQueue() {
        return new Queue(FRIEND_REQUEST_QUEUE);
    }

    @Bean
    public Binding bindingRequest(Queue friendRequestQueue, DirectExchange friendRequestExchange) {
        return BindingBuilder.bind(friendRequestQueue).to(friendRequestExchange).with(FRIEND_REQUEST_ROUTING_KEY);
    }

    // Friendship Response Configuration
    public static final String FRIENDSHIP_RESPONSE_EXCHANGE = "friendshipResponseExchange";
    public static final String FRIENDSHIP_RESPONSE_QUEUE = "friendshipResponseQueue";
    public static final String FRIENDSHIP_RESPONSE_ROUTING_KEY = "friendshipResponseKey";

    @Bean
    public DirectExchange friendshipEventExchange() {
        return new DirectExchange(FRIENDSHIP_RESPONSE_EXCHANGE);
    }

    @Bean
    public Queue friendshipEventQueue() {
        return new Queue(FRIENDSHIP_RESPONSE_QUEUE);
    }

    @Bean
    public Binding bindingResponse(Queue friendshipEventQueue, DirectExchange friendshipEventExchange) {
        return BindingBuilder.bind(friendshipEventQueue).to(friendshipEventExchange).with(FRIENDSHIP_RESPONSE_ROUTING_KEY);
    }

    public static final String USER_DELETE_EXCHANGE = "userDeleteExchange";
    public static final String USER_DELETE_QUEUE = "friendshipUserDeleteQueue";
    public static final String USER_DELETE_ROUTING_KEY = "userDeleteKey";

    @Bean
    public DirectExchange userDeleteExchange() {
        return new DirectExchange(USER_DELETE_EXCHANGE);
    }

    @Bean
    public Queue userDeleteQueue() {
        return new Queue(USER_DELETE_QUEUE);
    }

    @Bean
    public Binding bindingDelete(Queue userDeleteQueue, DirectExchange userDeleteExchange) {
        return BindingBuilder.bind(userDeleteQueue).to(userDeleteExchange).with(USER_DELETE_ROUTING_KEY);
    }

    public static final String USER_UPDATE_EXCHANGE = "userUpdateExchange";
    public static final String USER_UPDATE_QUEUE = "friendshipUserUpdateQueue";
    public static final String USER_UPDATE_ROUTING_KEY = "userUpdateKey";

    @Bean
    public DirectExchange userUpdateExchange() {
        return new DirectExchange(USER_UPDATE_EXCHANGE);
    }

    @Bean
    public Queue userUpdateQueue() {
        return new Queue(USER_UPDATE_QUEUE);
    }

    @Bean
    public Binding bindingUpdate(Queue userUpdateQueue, DirectExchange userUpdateExchange) {
        return BindingBuilder.bind(userUpdateQueue).to(userUpdateExchange).with(USER_UPDATE_ROUTING_KEY);
    }
}
