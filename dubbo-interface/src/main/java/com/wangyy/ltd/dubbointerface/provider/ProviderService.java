package com.wangyy.ltd.dubbointerface.provider;


import com.wangyy.ltd.dubbointerface.pojo.Consumer;

public interface ProviderService {
    
    Consumer getInfo(String name);
}
