package com.liuj.lmq.client;

import com.liuj.lmq.config.ProducerConfig;
import com.liuj.lmq.utils.PropUtil;

import java.util.Properties;

/**
 * Created by cdliujian1 on 2016/11/18.
 */
public class ProducerMain {

    public static void main(String[] args) {
        Properties prop = PropUtil.load("/producer.properties");
        String host = prop.getProperty("host");
        int port = Integer.valueOf(prop.getProperty("port"));
        ProducerConfig producerConfig = new ProducerConfig();
        producerConfig.setTopic("halou2");
        Producer producer = new Producer(host, port, 3000, producerConfig);
        producer.publish("{dali}");
    }

}
