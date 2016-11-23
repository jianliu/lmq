package com.liuj.lmq.spring.config;

import com.liuj.lmq.client.Consumer;
import com.liuj.lmq.client.MQConsumerClient;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * Created by cdliujian1 on 2016/11/23.
 */
public class LmqConsumerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return Consumer.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {

        String topic = element.getAttribute("topic");
        String corePoolSize = element.getAttribute("corePoolSize");
        String maxPoolSize = element.getAttribute("maxPoolSize");


        builder.addPropertyValue("topic", topic);
        builder.addPropertyValue("corePoolSize", Integer.valueOf(corePoolSize));
        builder.addPropertyValue("maxPoolSize", Integer.valueOf(maxPoolSize));


        String consumerClient = element.getAttribute("transport");
        builder.addPropertyValue("consumerClient", new RuntimeBeanReference(consumerClient));

        String listener = element.getAttribute("listener");
        builder.addPropertyValue("listener", new RuntimeBeanReference(listener));

        builder.setInitMethodName("registerListener");
    }
}