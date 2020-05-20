package com.wyy.ltd.rabbitmq.selfWrite.queue.work.roundRobin;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wyy.ltd.rabbitmq.selfWrite.util.ConnectionUtils;

public class WorkProducer {

    private static final String QUEUE_NAME = "WORK_QUEUE";
    
    public static void main(String[] args) throws Exception {
        Connection mqConnection = ConnectionUtils.getMqConnection();

        Channel channel = mqConnection.createChannel();
        
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        for (int i = 0; i < 50; i++) {
            
            String message = "message -> " + i;
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            Thread.sleep(i*10);
        }

        System.out.println("send over ......");
        channel.close();;
        mqConnection.close();
    }
    
}
