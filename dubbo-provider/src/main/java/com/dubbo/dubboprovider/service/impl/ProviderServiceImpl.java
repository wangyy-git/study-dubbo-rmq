package com.dubbo.dubboprovider.service.impl;


import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.config.annotation.Service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.wangyy.ltd.dubbointerface.pojo.Consumer;
import com.wangyy.ltd.dubbointerface.provider.ProviderService;
import org.springframework.stereotype.Component;

/**
 * 1、将服务提供者注册到注册中心(暴露服务)
 *      1 - 引入DUBBO依赖 dubbo-starter以及引入操纵ZK的客户端curator
 *      2 - 配置服务的提供者
 * 2、将服务消费者去注册中心订阅服务提供者的服务地址
 * 
 */

//@Service是DUBBO的service注解 暴露服务
@Service
@Component
public class ProviderServiceImpl implements ProviderService {
    @HystrixCommand
    @Override
    public Consumer getInfo(String name) {
        System.out.println(" 3 号被调用 ......");
        Consumer consumer = new Consumer();
        consumer.setName(name);
        consumer.setAge(8);
        return consumer;
    }
}
