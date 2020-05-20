package com.wyy.ltd.rabbitmq.boot;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Publisher {
    
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(String message){
        System.out.println("发送消息："+message);
        //void convertAndSend(String routingKey, final Object object)
        amqpTemplate.convertAndSend("direct",message);
    }
}
