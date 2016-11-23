package com.liuj.lmq.spring;

import com.liuj.lmq.client.Producer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by cdliujian1 on 2016/11/23.
 */
public class ConsumerMain {


    public static void main(String[] args) {
        ApplicationContext applicationContext  = new ClassPathXmlApplicationContext("classpath:spring/spring-lmq-consumer.xml");

        System.out.println("consumer will working now.");
    }


}
