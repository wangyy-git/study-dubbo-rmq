package com.wyy.ltd.rabbitmq.selfWrite.queue.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wyy.ltd.rabbitmq.selfWrite.util.ConnectionUtils;

public class SimpleQueueProducer {

    private static final String QUEUE_NAME = "SIMPLE_QUEUE";
    public static void main(String[] args) throws Exception {
        Connection mqConnection = ConnectionUtils.getMqConnection();
        
        //从连接中获取一个通道
        Channel channel = mqConnection.createChannel();
        
        //创建队列exclusive
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        
        String message = "also hello word";
        
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());

        System.out.println("send message .... ");
        channel.close();
        mqConnection.close();
    }
}
