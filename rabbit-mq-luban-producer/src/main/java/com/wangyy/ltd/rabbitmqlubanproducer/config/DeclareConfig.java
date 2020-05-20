package com.wangyy.ltd.rabbitmqlubanproducer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class DeclareConfig {

    /**
     * 申请一个死信交换机
     */
    @Bean
    public Queue queue(){
        Map<String,Object> map = new HashMap<>();
        //前面的这个可以可以参考管理页面创建queue提示的参数
        map.put("x-dead-letter-exchange","dead-letter-exchange");
        //queueName 是否持久化
        return new Queue("boot_queue",true,false,false,map);
    }
    
    @Bean
    public Exchange defaultExchange(){
        //备用交换机
        Map<String,Object> map = new HashMap<>();
        //需要将次exchange额外声明
        map.put("alternate-exchange","standby-exchange");
        TopicExchange exchange = new TopicExchange("boot_exchange",true,false,map);
        return exchange;
    }

    /**
     * 绑定一个队列
     * to 后面绑定的交换机
     * with 指定的routingKey
     */
    @Bean
    public Binding binding(){
        
        return BindingBuilder.bind(queue())
                .to(defaultExchange())
                .with("goods.*")
                .noargs();
    }

    @Autowired
    private ConnectionFactory connectionFactory;
    /**
     * 消息监听器的容器
     */
//    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(){
        SimpleMessageListenerContainer sim = new SimpleMessageListenerContainer();
        sim.setConnectionFactory(connectionFactory);
        sim.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println("容器" + new String(message.getBody()));
            }
        });
        sim.addQueues(queue());
        //  sim.addQueueNames(); //绑定多个queue
        //设置手动返回
        sim.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return sim;
    }

    /**
     * 与上面bean效果一样，实现手动返回
     * 但是可以使用@RabbitListener注解使得更简洁
     */
    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(){
        SimpleRabbitListenerContainerFactory sim = new SimpleRabbitListenerContainerFactory();
        //相关也可在properties文件中配置
        sim.setConnectionFactory(connectionFactory);
        sim.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //设置消息预取数量
        sim.setPrefetchCount(1);
        
        return sim;
    }
}
