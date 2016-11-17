package com.liuj.lmq.client;

import java.util.List;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public interface IMessageListener {

    /**
     * 接受到消息
     * @param message
     */
    void onMessage(List<Object> message);

}
