package com.liuj.lmq.spring.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by cdliujian1 on 2016/11/23.
 */
public class LmqNamespceHandler extends NamespaceHandlerSupport{

    public void init() {
        registerBeanDefinitionParser("producer",
                new LmqProducerBeanDefinitionParser());

        registerBeanDefinitionParser("server",new LmqServerBeanDefinitionParser());

        registerBeanDefinitionParser("consumerClient",new LmqClientBeanDefinitionParser());

        registerBeanDefinitionParser("consumer",new LmqConsumerBeanDefinitionParser());


    }

}
