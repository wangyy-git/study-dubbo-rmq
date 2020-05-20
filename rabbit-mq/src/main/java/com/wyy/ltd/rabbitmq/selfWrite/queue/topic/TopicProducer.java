package com.wyy.ltd.rabbitmq.selfWrite.queue.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wyy.ltd.rabbitmq.selfWrite.util.ConnectionUtils;

public class TopicProducer {

    private static final String EXCHANGE_NAME = "EXCHANGE_TOPIC";
    private static final String ROUTING_KEY = "goods.add.1";
    
    public static void main(String[] args) throws Exception {
        Connection mqConnection = ConnectionUtils.getMqConnection();
        Channel channel = mqConnection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String msg = "商品 操作 ....";
        channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY,null,msg.getBytes());

        System.out.println("send over ...." + msg + ROUTING_KEY);
        channel.close();
        mqConnection.close();

    }
}
