package com.wyy.ltd.rabbitmq.selfWrite.transaction;

import com.rabbitmq.client.*;
import com.wyy.ltd.rabbitmq.selfWrite.util.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TxRev {

    private static final String ROUTING_KEY = "tx";

    public static void main(String[] args) throws Exception {
        Connection mqConnection = ConnectionUtils.getMqConnection();
        Channel channel = mqConnection.createChannel();

        channel.queueDeclare(ROUTING_KEY,false,false,false,null) ;
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println(message);
            }
        };
        
        // 监听队列
        channel.basicConsume(ROUTING_KEY,true,consumer);

    }
}
