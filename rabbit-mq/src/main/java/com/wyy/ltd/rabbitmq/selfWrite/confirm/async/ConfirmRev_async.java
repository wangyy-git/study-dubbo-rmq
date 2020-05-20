package com.wyy.ltd.rabbitmq.selfWrite.confirm.async;

import com.rabbitmq.client.*;
import com.wyy.ltd.rabbitmq.selfWrite.util.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ConfirmRev_async {

    private static final String QUEUE_NAME = "async_confirm";

    public static void main(String[] args) throws Exception {
        Connection mqConnection = ConnectionUtils.getMqConnection();
        Channel channel = mqConnection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null) ;
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                System.out.println(new String(body, StandardCharsets.UTF_8));
            }
        };
        
        // 监听队列
        channel.basicConsume(QUEUE_NAME,true,consumer);

    }
}
