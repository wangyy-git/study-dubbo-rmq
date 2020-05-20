package com.wyy.ltd.rabbitmq.selfWrite.transaction;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wyy.ltd.rabbitmq.selfWrite.util.ConnectionUtils;

public class TxProducer {

    private static final String ROUTING_KEY = "tx";
    public static void main(String[] args) throws Exception {

        Connection mqConnection = ConnectionUtils.getMqConnection();
        Channel channel = mqConnection.createChannel();
        AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();
        channel.queueDeclare(ROUTING_KEY,false,false,false,null);
        String msg = "tx send";
        
        try{
            channel.txSelect();
            channel.basicPublish("",ROUTING_KEY,null,msg.getBytes());

            System.out.println("msg send -> " + msg);
            channel.txCommit();
            
        }catch (Exception e) {

            System.out.println("send error");
            channel.txRollback();
        }
        
        channel.close();;
        mqConnection.close();
    }
}
