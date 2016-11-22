package com.liuj.lmq.client;

import com.liuj.lmq.server.ServerManager;
import com.liuj.lmq.bean.MessageResult;
import com.liuj.lmq.core.Message;
import com.liuj.lsf.core.AbstractLogger;
import com.liuj.lsf.core.ResponseListener;
import com.liuj.lsf.msg.ResponseMsg;
import com.liuj.lsf.transport.ClientTransport;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by cdliujian1 on 2016/11/22.
 */
public class MQResponseListener extends AbstractLogger implements ResponseListener{

    public void onResponse(ClientTransport clientTransport, ResponseMsg responseMsg) {
        Object data = responseMsg.getResponse();
        if (data instanceof Message) {
            Message message = (Message) data;

            LinkedBlockingDeque<Message> messageQueue = ServerManager.checkMessageTopic(message.getTopic());
            logger.debug("logging queue size,topic:{}, size:{}", message.getTopic(), messageQueue.size());
            messageQueue.add(message);
        }else if(data instanceof MessageResult){
            MessageResult result = (MessageResult) data;
            logger.debug("send message get result,topic:{},handled:{}", result.getTopic(), result.getHandled());
        }
    }
}
