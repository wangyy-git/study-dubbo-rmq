package com.wyy.ltd.rabbitmq.selfWrite.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wyy.ltd.rabbitmq.selfWrite.util.ConnectionUtils;

public class ConfirmProducerBatch {

    private static final String ROUTING_KEY = "confirm_1";
    public static void main(String[] args) throws Exception {

        Connection mqConnection = ConnectionUtils.getMqConnection();
        Channel channel = mqConnection.createChannel();
        channel.queueDeclare(ROUTING_KEY,false,false,false,null);
        //将channel设置为confirm模式
        channel.confirmSelect();

        for (int i = 0; i < 10; i++) {
            String msg = "confirm model ..." + i;
            channel.basicPublish("",ROUTING_KEY,null,msg.getBytes());
        }
        if (!channel.waitForConfirms()) {
            System.out.println("confirm model send fail ...");
        } else {
            System.out.println("send over ....");
        }
        
        channel.close();;
        mqConnection.close();
    }
}
