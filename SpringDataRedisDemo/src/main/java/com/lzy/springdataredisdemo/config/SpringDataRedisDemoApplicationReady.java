package com.lzy.springdataredisdemo.config;

import com.lzy.springdataredisdemo.event.BusEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class SpringDataRedisDemoApplicationReady implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    RedisMessageListenerContainer container;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(new BusEvent());
        container.addMessageListener(adapter,new PatternTopic("mytopic"));
    }
}
