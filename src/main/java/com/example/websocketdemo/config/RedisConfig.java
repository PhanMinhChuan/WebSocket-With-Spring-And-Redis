package com.example.websocketdemo.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

@Configuration
@Profile("!embedded")
public class RedisConfig {

    @Bean
    public Jackson2JsonRedisSerializer<Message> messageRedisSerializer() {

        // For Java8 date serialization
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        // objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
        // objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        // objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //

        Jackson2JsonRedisSerializer<Message> serializer = new Jackson2JsonRedisSerializer<>(Message.class);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    @Bean
    public RedisTemplate<String, Message> messageRedisTemplate(RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, Message> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setValueSerializer(messageRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(NewMessageEventListener eventListener) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(eventListener);
        adapter.setSerializer(messageRedisSerializer());
        return adapter;
    }

    @Bean
    public MessageListenerAdapter messageBroadcasterAdapter(NewMessageBroadcaster eventListener) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(eventListener);
        adapter.setSerializer(messageRedisSerializer());
        return adapter;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory, NewMessageEventListener eventListener,
            NewMessageBroadcaster broadcastListener) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter(eventListener),
                Arrays.asList(new ChannelTopic(NewMessageEventListener.EVENT_RECEIVE_MESSAGE_KEY)));
        container.addMessageListener(messageBroadcasterAdapter(broadcastListener),
                Arrays.asList(new ChannelTopic(NewMessageBroadcaster.EVENT_RECEIVE_MESSAGE_KEY)));

        return container;
    }
}
