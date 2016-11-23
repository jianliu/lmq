package com.liuj.lmq.spring;

import com.liuj.lmq.client.Producer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by cdliujian1 on 2016/11/23.
 */
public class ProduceMain {


    public static void main(String[] args) {
        ApplicationContext applicationContext  = new ClassPathXmlApplicationContext("classpath:spring/spring-lmq-producer.xml");
        Producer producer = (Producer) applicationContext.getBean("producer");
        producer.publish("a");
    }


}
