package com.liuj.lmq.server;

import io.netty.channel.Channel;

/**
 * Created by cdliujian1 on 2016/11/18.
 */
public interface RequestHandle {

    Object NULL= new Object();

    Object handleRequest(Object data, Channel channel);

}
