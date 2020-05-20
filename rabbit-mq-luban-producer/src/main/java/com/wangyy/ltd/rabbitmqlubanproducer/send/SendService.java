package com.wangyy.ltd.rabbitmqlubanproducer.send;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SendService {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void send(String message,String exchaneName,String routingKey){
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("这是第100次发送");

        //springboot会自己转化是转化为Message对象进行了分装
        //但是建议自己使用JSON转化
//        Map<String,String> map = new HashMap<>();
//        map.put("msg",message);
        for (int i = 0;
             
             i <20;i++) {
            rabbitTemplate.convertAndSend(exchaneName,routingKey,(message + (i + 1)),correlationData);
        }
    }
}
