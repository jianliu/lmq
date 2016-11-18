package com.liuj.lmq.client;

import com.liuj.lmq.core.Callback;
import com.liuj.lsf.openapi.IClient;

import java.util.List;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public class MessageInvoker {

    private IClient client;

    public void invoke(IMessageListener messageListener, List<Object> messages, Callback callback){
        messageListener.onMessage(null);
        callback.callback();
    }

}
