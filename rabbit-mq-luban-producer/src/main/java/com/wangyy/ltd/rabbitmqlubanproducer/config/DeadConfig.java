package com.wangyy.ltd.rabbitmqlubanproducer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeadConfig {
    
    @Bean
    public Queue deadQueue(){
        
        
        String queueName = "dead-queue";

        return new Queue(queueName,true,false,false);
    }
    
    @Bean
    public Exchange deadExchange(){
        return new TopicExchange("dead-letter-exchange");
    }
    
    @Bean
    public Binding deadBinding(){
        return BindingBuilder.bind(deadQueue())
                .to(deadExchange())
                .with("goods.dead")
                .noargs();
    }
}
