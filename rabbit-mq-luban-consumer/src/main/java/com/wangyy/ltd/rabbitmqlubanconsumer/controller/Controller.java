package com.wangyy.ltd.rabbitmqlubanconsumer.controller;

import com.wangyy.ltd.rabbitmqlubanconsumer.config.RabbitMqConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ResponseBody
@RestController
public class Controller {

    @Autowired
    private RabbitMqConfig config;
    
    
}
