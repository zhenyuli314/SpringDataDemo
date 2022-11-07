package com.lzy.springdataredisdemo.event;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.Arrays;

public class BusEvent implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println(new String(message.getChannel()));
        System.out.println(new String(message.getBody()));
    }
}
