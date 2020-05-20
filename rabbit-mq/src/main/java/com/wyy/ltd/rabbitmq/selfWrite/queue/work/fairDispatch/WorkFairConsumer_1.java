package com.wyy.ltd.rabbitmq.selfWrite.queue.work.fairDispatch;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.wyy.ltd.rabbitmq.selfWrite.util.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WorkFairConsumer_1 {

    private static final String QUEUE_NAME = "WORK_QUEUE";

    public static void main(String[] args) throws Exception {

        Channel channel = ConnectionUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        
        channel.basicQos(1);
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
