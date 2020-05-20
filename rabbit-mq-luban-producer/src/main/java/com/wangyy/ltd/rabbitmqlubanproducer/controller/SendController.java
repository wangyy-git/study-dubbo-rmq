package com.wangyy.ltd.rabbitmqlubanproducer.controller;

import com.wangyy.ltd.rabbitmqlubanproducer.send.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@ResponseBody
@RestController
public class SendController {
    
    @Autowired
    private SendService sendService;
    
//    @Value("${rabbitmq.send.routing-key}")
//    private String routingKey;

    @Value("${rabbitmq.send.exchange.name}")
    private String exchangeName;
    
    @RequestMapping(value = "/send")
    public String sendOrderController(String message,String routingKey){
        System.out.println("send --> " + exchangeName + "        " + routingKey);
        
        sendService.send(message,exchangeName,routingKey);
        return "send success";
    }
}
