package com.liuj.lmq.spring.config;

import com.liuj.lmq.bean.Server;
import com.liuj.lmq.client.Producer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * Created by cdliujian1 on 2016/11/23.
 */
public class LmqServerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return Server.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {

        String index = element.getAttribute("index");
        String port = element.getAttribute("port");

        if(StringUtils.isBlank(index)){
            throw new IllegalArgumentException("lmq server index must not be null.");
        }

        if(!StringUtils.isNumeric(port)){
            throw new IllegalArgumentException("lmq server port must not be number.");
        }
        //        new RuntimeBeanReference("");
        builder.addPropertyValue("index", index);
        builder.addPropertyValue("port", Integer.valueOf(port));

    }
}
