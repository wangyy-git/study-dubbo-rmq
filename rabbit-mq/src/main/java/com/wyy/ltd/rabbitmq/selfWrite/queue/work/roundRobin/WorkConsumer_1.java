package com.wyy.ltd.rabbitmq.selfWrite.queue.work.roundRobin;

import com.rabbitmq.client.*;
import com.wyy.ltd.rabbitmq.selfWrite.util.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WorkConsumer_1 {

    private static final String QUEUE_NAME = "WORK_QUEUE";

    public static void main(String[] args) throws Exception {

        Channel channel = ConnectionUtils.getChannel();
        
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        DefaultConsumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);

                System.out.println("[1] Rev --> " + message);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        
        boolean autoAck = true;
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);
        
    }
}
