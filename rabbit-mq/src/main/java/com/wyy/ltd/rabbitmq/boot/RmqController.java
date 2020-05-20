package com.wyy.ltd.rabbitmq.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
public class RmqController {
    @Autowired
    private Publisher publisher;
    @RequestMapping(value = "/rmq")
    public String getRev(String msg){
        System.out.println("send msg -> " + msg);
        publisher.send(msg);
        
        return "ok";
    }
}
