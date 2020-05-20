package com.wyy.ltd.dubboorderconsumer.webConsumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.wangyy.ltd.dubbointerface.pojo.Consumer;
import com.wangyy.ltd.dubbointerface.provider.ProviderService;
import com.wyy.ltd.dubboorderconsumer.stub.ProviderServiceStub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Reference(loadbalance = "random",stub = "com.wyy.ltd.dubboorderconsumer.stub.ProviderServiceStub")
    private ProviderService providerService;

    @RequestMapping(value = "/consumer")
    public Consumer getInfo(String name){
        return providerService.getInfo(name);
    }
    
     
    @RequestMapping(value = "/stub")
    public Consumer getStub(String name){
        if (Math.random() < 0.5) {
            throw new RuntimeException();
        }
        return providerService.getInfo(name);
    }
    
    
    public Consumer fallback(String name){
        System.out.println(name);
        Consumer con = new Consumer();
        con.setName("fallback");
        return con;
    }
}
