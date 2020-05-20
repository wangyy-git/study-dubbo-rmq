package com.dubbo.dubboprovider.controller;

import com.wangyy.ltd.dubbointerface.pojo.Consumer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/service")
public class ProviderController {
    
    @RequestMapping(value = "/name",method = RequestMethod.POST)
    public Consumer getInfo(String name){
        Consumer consumer = new Consumer();
        consumer.setName(name);
        consumer.setAge(8);
        
        return consumer;
    }
    
    
}
