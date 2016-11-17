package com.liuj.lmq.client;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public interface IProducer {

    /**
     * 重连接
     */
    void reconnect();

    /**
     * 发布一条消息
     * @param data
     */
    void publish(Object data);

    /**
     * 发布一条消息
     * @param data
     */
    void addMessage(Object data);


}
