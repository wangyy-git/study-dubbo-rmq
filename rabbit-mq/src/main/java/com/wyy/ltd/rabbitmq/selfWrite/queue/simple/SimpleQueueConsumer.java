package com.wyy.ltd.rabbitmq.selfWrite.queue.simple;

import com.rabbitmq.client.*;
import com.wyy.ltd.rabbitmq.selfWrite.util.ConnectionUtils;

import java.io.IOException;

public class SimpleQueueConsumer {
    private static final String QUEUE_NAME = "SIMPLE_QUEUE";

    public static void main(String[] args) throws Exception {
        Connection mqConnection = ConnectionUtils.getMqConnection();

        Channel channel = mqConnection.createChannel();
        
        channel.queueDeclare(QUEUE_NAME,false,false,false,null) ;
        // 定义队列的消费者
        final String[] message = new String[1];
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                message[0] = new String(body,"UTF-8");
                System.out.println(message[0]);
                System.out.println("consumerTag -> " + consumerTag);
                System.out.println("properties -> " + properties);
                System.out.println("envelope -> " + envelope);
            }
        };

//        System.out.println(message[0]);
//        QueueingConsumer consumer = new QueueingConsumer(channel);

        // 监听队列
        channel.basicConsume(QUEUE_NAME,true,consumer);
        

//        // 获取消息
//        while (true) {
//            consumer.handleDelivery();
////            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
////            String message = new String(delivery.getBody());
////            System.out.println(" [x] Received '" + message + "'");
//        }
        
    }
}
