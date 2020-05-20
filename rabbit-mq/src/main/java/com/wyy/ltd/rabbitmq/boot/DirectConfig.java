package com.wyy.ltd.rabbitmq.boot;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectConfig {
    
    @Bean
    public Queue directQueue(){
        String queueName = "direct";
        return new Queue(queueName,false);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("direct_ex",false,false);//交换器名称、是否持久化、是否自动删除
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("direct_key");
    }
}
