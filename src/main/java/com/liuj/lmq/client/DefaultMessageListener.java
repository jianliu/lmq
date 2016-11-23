package com.liuj.lmq.client;

import com.liuj.lmq.core.Message;
import com.liuj.lsf.core.AbstractLogger;

/**
 * Created by cdliujian1 on 2016/11/23.
 */
public class DefaultMessageListener extends AbstractLogger implements IMessageListener{

    public void onMessage(Message message) {
        logger.info("handle message,id:{},text:{}", message.getMessageId(), message.getText());
    }

}
