package com.wyy.ltd.rabbitmq.selfWrite.queue.work.fairDispatch;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wyy.ltd.rabbitmq.selfWrite.util.ConnectionUtils;

public class WorkFairProducer {

    private static final String QUEUE_NAME = "WORK_QUEUE";
    
    public static void main(String[] args) throws Exception {
        Connection mqConnection = ConnectionUtils.getMqConnection();

        Channel channel = mqConnection.createChannel();
        
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        
        //每个消费者在发送确认消息之前，producer不会发生新的消息给consumer;
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
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
