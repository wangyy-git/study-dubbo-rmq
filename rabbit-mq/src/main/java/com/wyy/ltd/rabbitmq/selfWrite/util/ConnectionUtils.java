package com.wyy.ltd.rabbitmq.selfWrite.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class ConnectionUtils {
    
    public static Connection getMqConnection() {
        ConnectionFactory factory = new ConnectionFactory();
        
        //设置服务地址
        factory.setHost("192.168.195.108");
        //设置端口 AMQP的端口
        factory.setPort(5672);
        //设置账号信息，用户名/密码/vhosts
        factory.setUsername("wyy");
        factory.setPassword("123456");
        factory.setVirtualHost("/wyy");

        Connection connection;
        try {
             connection = factory.newConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            connection = null;
        }
        return connection;
    }
    
    
    public static Channel getChannel(){
        Connection mqConnection = getMqConnection();

        Channel channel;
        try {
            channel = mqConnection.createChannel();
        } catch (IOException e) {
            channel = null;
            e.printStackTrace();
        }
        
        return channel;
    }
}
