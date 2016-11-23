package com.liuj.lmq.client;

import com.liuj.lmq.bean.Server;
import com.liuj.lmq.config.ConsumerConfig;
import com.liuj.lmq.core.Message;
import com.liuj.lmq.utils.PropUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by cdliujian1 on 2016/11/18.
 */
public class ConsumerMain {

    private final static Logger logger = LoggerFactory.getLogger(ConsumerMain.class);

    public static void main(String[] args) {
        Properties prop = PropUtil.load("/consumer.properties");
        String host = prop.getProperty("host");
        int port = Integer.valueOf(prop.getProperty("port"));
        String topic = prop.getProperty("topic");
        MQConsumerClient consumerClient = new MQConsumerClient(new Server(host,port),5000);
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setTopic(topic);
        consumerClient.registerListener(consumerConfig, new IMessageListener() {
            public void onMessage(Message message) {
                logger.info("handle message,id:{},text:{}", message.getMessageId(), message.getText());
            }
        }, false);
        consumerClient.startWork();
    }


}
