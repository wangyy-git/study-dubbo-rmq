package com.wyy.ltd.rabbitmq.selfWrite.queue.publish_subscribe;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wyy.ltd.rabbitmq.selfWrite.util.ConnectionUtils;

public class PublishProducer {

//    private static final String QUEUE
    private static final String EXCHANGE_NAME = "EXCHANGE_FANOUT";
    
    public static void main(String[] args) throws Exception {

        Connection mqConnection = ConnectionUtils.getMqConnection();
        Channel channel = mqConnection.createChannel();
        //声明exchange 
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        
        String message = "rabbit p-s model";
        channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());

        System.out.println("send over ....");
        
        channel.close();
        mqConnection.close();
        
    }
}
