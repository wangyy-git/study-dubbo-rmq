package com.wyy.ltd.rabbitmq.selfWrite.queue.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wyy.ltd.rabbitmq.selfWrite.util.ConnectionUtils;

public class RoutingProducer {

    private static final String EXCHANGE_NAME = "EXCHANGE_DIRECT";
    private static final String ROUTING_KEY = "INFO";

    public static void main(String[] args) throws Exception {
        Connection mqConnection = ConnectionUtils.getMqConnection();
        Channel channel = mqConnection.createChannel();
        
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        
        String msg = "this direct model";
        channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY,null,msg.getBytes());

        System.out.println("send over ....");
        channel.close();
        mqConnection.close();
        
    }
}
