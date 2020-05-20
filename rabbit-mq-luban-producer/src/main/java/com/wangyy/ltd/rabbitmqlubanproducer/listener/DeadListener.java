package com.wangyy.ltd.rabbitmqlubanproducer.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DeadListener {
    @RabbitListener(queues = "dead-queue",containerFactory = "simpleRabbitListenerContainerFactory")
    public void getDeadMessage(Message msg, Channel channel) throws IOException {
        
        System.out.println("dead-queue --> " + new String(msg.getBody()));
        long deliveryTag = msg.getMessageProperties().getDeliveryTag();
        channel.basicAck(deliveryTag,false);
 
    }
}
