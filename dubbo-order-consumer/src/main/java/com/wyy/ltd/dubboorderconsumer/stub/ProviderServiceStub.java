package com.wyy.ltd.dubboorderconsumer.stub;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wangyy.ltd.dubbointerface.pojo.Consumer;
import com.wangyy.ltd.dubbointerface.provider.ProviderService;
import org.springframework.stereotype.Component;

/**
 * 本地存根
 */
public class ProviderServiceStub implements ProviderService {

    
    private final ProviderService providerService;

    /**
     * @param providerService 的远程proxy对象
     */
    
    public ProviderServiceStub(ProviderService providerService) {
        this.providerService = providerService;
    }

    @Override
    public Consumer getInfo(String name) {
        System.out.println("local stub is invoked .... ");
        Consumer con = new Consumer();
        if ("m".equals(name)) {
            con.setName("this is stub set");
            con.setAge(1000);
            return con;
        }
        
        return providerService.getInfo(name);
    }
}
