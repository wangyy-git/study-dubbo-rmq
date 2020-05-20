package com.wangyy.ltd.rabbitmqlubanproducer.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    //springboot会自动获取配置并注入ConnectionFactory
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate();
        System.out.println("消息确认 -> " + connectionFactory.isPublisherConfirms());
        template.setConnectionFactory(connectionFactory);
        
        //消息确认模式
        //发送成功或者失败都会回调
        //设置消息发送成功的回调方法
        template.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                //CorrelationData可以在send处将订单ID的业务参数记录其中
                //以方便获取哪一个消息发送失败了
//                System.out.println("correlationData -> " + correlationData);
//                System.out.println("ack -> " + ack);
//                System.out.println("cause -> " + cause);
                //将失败信息入库等
            }
        });
        
        //开始失败确认
        template.setMandatory(true);
        //也可以写一个实现类
        template.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                //此处的message 相当于发送的msg + msg的配置
                //replyCode 状态码
            }
        });
        
        //设置消息转化
        //发送消息是toMessage  接收消息是fromMessage
        template.setMessageConverter(new MessageConverter() {
            //这个Object就是send传入的Object
            @Override
            public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {

//                System.out.println("message Object -> " + object);
//                System.out.println("message MessageProperties -> " + messageProperties);
//                
                return new Message(JSON.toJSONString(object).getBytes(),messageProperties);
            }

            //
            @Override
            public Object fromMessage(Message message) throws MessageConversionException {
//                JSON.parseObject(message.getBody());
                message.getMessageProperties();
                return null;
            }
        });
        return template;
    }
}
