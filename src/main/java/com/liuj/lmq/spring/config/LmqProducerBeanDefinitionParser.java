package com.liuj.lmq.spring.config;

import com.liuj.lmq.client.MQConsumerClient;
import com.liuj.lmq.client.Producer;
import com.liuj.lmq.config.ProducerConfig;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * Created by cdliujian1 on 2016/11/23.
 */
public class LmqProducerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return Producer.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {

        String topic = element.getAttribute("topic");
        String server = element.getAttribute("server");

        builder.addPropertyValue("server", new RuntimeBeanReference(server));

        ProducerConfig producerConfig = new ProducerConfig();
        producerConfig.setTopic(topic);
        builder.addPropertyValue("producerConfig",producerConfig);
       // builder.addPropertyReference("server",server);
        builder.setInitMethodName("init");
    }
}
