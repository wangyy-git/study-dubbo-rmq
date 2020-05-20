package com.wangyy.ltd.rabbitmqlubanproducer.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 两个listener会轮流获取
 * 类似于简单队列round-robin模式
 * 若不设置自动应答则为round-robin模式
 * 可设置预取和手动应答实现按劳分配
 */
@Component
public class Listener {
    
    private Integer i = 1;
    private Integer j = 1;
    //在消费者这边才需要制定queue_name
    // producer只需要指定exchangeName和routing_key
//    @RabbitHandler
//    @RabbitListener(queues = "boot_queue",containerFactory = "simpleRabbitListenerContainerFactory")
    public void listen(String msg,Channel channel) throws IOException {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println( (i++) +"  msg --> " + msg);
        channel.basicAck(i-1,false);
    }
    
    @RabbitListener(queues = "boot_queue",containerFactory = "simpleRabbitListenerContainerFactory")
    public void getMessage(Message msg, Channel channel) throws IOException {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //对应的消息在Message的body里面
//        System.out.println("delivery -> " + msg.getMessageProperties().getDeliveryTag());
        System.out.println("Message 1s --> " + new String(msg.getBody()));
//        System.out.println(new String(msg.getBody()));
        long deliveryTag = msg.getMessageProperties().getDeliveryTag();
        //实际场景中，如果成功了则确认，如执行失败则不确认
        //如果最后参数为true 退回队列 则应该写一个一定消费的消费者 防止死循环
        if(handler()) {
            channel.basicAck(deliveryTag,false);
        } else {
            //退回 单条退回
            System.out.println("boot_queue 退回消息");
            //批量退回
//            channel.basicReject(deliveryTag,false);
        }
        
        channel.basicQos(1);
//        channel.basicConsume();
        
    }


    @RabbitListener(queues = "boot_queue",containerFactory = "simpleRabbitListenerContainerFactory")
    public void getMessage2(Message msg, Channel channel) throws IOException {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //对应的消息在Message的body里面
//        System.out.println("delivery -> " + msg.getMessageProperties().getDeliveryTag());
        System.out.println("Message 200ms --> " + new String(msg.getBody()));
//        System.out.println(new String(msg.getBody()));
        long deliveryTag = msg.getMessageProperties().getDeliveryTag();
        //实际场景中，如果成功了则确认，如执行失败则不确认
        //如果最后参数为true 退回队列 则应该写一个一定消费的消费者 防止死循环
        channel.basicQos(1);
        channel.basicAck(deliveryTag,false);
        
    }
    
    
    private boolean handler(){
        return true;
    }
}
