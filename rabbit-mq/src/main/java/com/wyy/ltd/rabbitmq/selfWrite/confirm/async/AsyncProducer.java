package com.wyy.ltd.rabbitmq.selfWrite.confirm.async;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.wyy.ltd.rabbitmq.selfWrite.util.ConnectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class AsyncProducer {

    private static final String QUEUE_NAME = "async_confirm";
    public static void main(String[] args) throws Exception {
        Connection mqConnection = ConnectionUtils.getMqConnection();
        Channel channel = mqConnection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        channel.confirmSelect();
        
        //未确认消息标识记录
        final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<>());
        
        channel.addConfirmListener(new ConfirmListener() {
            //没有问题的handleAck
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {

                System.out.println("deliveryTag -> " + deliveryTag);
                System.out.println("multiple -> " + multiple);
                if (multiple) {
                    System.out.println("handleAck ----------- multiple  ....");
                    confirmSet.headSet(deliveryTag +1).clear();
                } else {
                    System.out.println("handleAck ----------- multiple  false");
                    confirmSet.remove(deliveryTag);
                }
                
            }
            
            //回执有问题的
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("handleNack deliveryTag -> " + deliveryTag);
                System.out.println("handleNack multiple -> " + multiple);
                if (multiple) {
                    System.out.println("handleNack ----------- multiple  ....");
                    confirmSet.headSet(deliveryTag +1).clear();
                } else {
                    System.out.println("handleNack ----------- multiple  false");
                    confirmSet.remove(deliveryTag);
                }
            }
        });

        String msg = "confirm async ...";
        while (true){
            long publishSeqNo = channel.getNextPublishSeqNo();
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
            //发送成功要记录
            confirmSet.add(publishSeqNo);
        }
    }
}
