package com.liuj.lmq.client;

import com.liuj.lmq.config.ConsumerConfig;
import com.liuj.lsf.openapi.IClient;

import java.util.List;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public interface IConsumerClient {


    /**
     * 注册主题的消息监听器
     * @param topic
     * @param messageListener
     */
    void registerListener(ConsumerConfig topic, IMessageListener messageListener);
    /**
     * 重连接
     */
    void reconnect();

    /**
     * 获取消息处理
     */
    void startWork();


    /**
     *
     * @return
     */
    IClient getClient();
}
