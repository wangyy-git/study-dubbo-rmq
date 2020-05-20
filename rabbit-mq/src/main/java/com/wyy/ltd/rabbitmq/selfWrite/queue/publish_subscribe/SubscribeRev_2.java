package com.wyy.ltd.rabbitmq.selfWrite.queue.publish_subscribe;

import com.rabbitmq.client.*;
import com.wyy.ltd.rabbitmq.selfWrite.util.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class SubscribeRev_2 {

    private static final String QUEUE_NAME = "queue_fanout_sms";
    private static final String EXCHANGE_NAME = "EXCHANGE_FANOUT";

    public static void main(String[] args) throws Exception {
        Connection mqConnection = ConnectionUtils.getMqConnection();
        Channel channel = mqConnection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        
        //绑定队列到exchange
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");
        channel.basicQos(1);
        DefaultConsumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("[2] Rev --> " + message);

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
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);
    }
    
}
