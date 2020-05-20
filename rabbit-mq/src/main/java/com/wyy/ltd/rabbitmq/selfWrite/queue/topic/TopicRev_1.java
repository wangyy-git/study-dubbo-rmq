package com.wyy.ltd.rabbitmq.selfWrite.queue.topic;

import com.rabbitmq.client.*;
import com.wyy.ltd.rabbitmq.selfWrite.util.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TopicRev_1 {

    private static final String EXCHANGE_NAME = "standby-exchange";
    private static final String ROUTING_KEY = "goods.*";
    private static final String QUEUE_NAME = "topic_1";
    
    public static void main(String[] args) throws Exception {
        Connection mqConnection = ConnectionUtils.getMqConnection();
        Channel channel = mqConnection.createChannel();
        
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        channel.basicQos(1);
        
        //消费者只能绑定queue  不能绑定exchange
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);

        DefaultConsumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("[1] topic Rev --> " + message);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //设置应答
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };

        boolean autoAck = false;//自动应答关掉
        //开始消费
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);
    }
}
