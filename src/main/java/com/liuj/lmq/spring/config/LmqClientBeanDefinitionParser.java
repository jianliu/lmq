package com.liuj.lmq.spring.config;

import com.liuj.lmq.client.MQConsumerClient;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * Created by cdliujian1 on 2016/11/23.
 */
public class LmqClientBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return MQConsumerClient.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {

        String server = element.getAttribute("server");
        builder.addPropertyValue("server", new RuntimeBeanReference(server));

        builder.setInitMethodName("init");
    }
}