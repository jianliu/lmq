package com.liuj.lmq.server;

import com.liuj.lmq.bean.MessageResult;
import com.liuj.lmq.bean.ProduceData;
import com.liuj.lmq.bean.Subscribe;
import io.netty.channel.Channel;

/**
 * Created by cdliujian1 on 2016/11/18.
 */
public class ServerRequestHandle implements RequestHandle {

    private ServerMessageHolder serverDataHolder;

    private ServerConnectHolder serverConnectHolder;

    public ServerRequestHandle(ServerMessageHolder serverDataHolder, ServerConnectHolder serverConnectHolder) {
        this.serverDataHolder = serverDataHolder;
        this.serverConnectHolder = serverConnectHolder;
    }

    public void setServerDataHolder(ServerMessageHolder serverDataHolder) {
        this.serverDataHolder = serverDataHolder;
    }

    public void setServerConnectHolder(ServerConnectHolder serverConnectHolder) {
        this.serverConnectHolder = serverConnectHolder;
    }

    public Object handleRequest(Object data, Channel channel) {
        if (data instanceof ProduceData) {
            ProduceData produceData = (ProduceData) data;
            String messageId = serverDataHolder.add(produceData);
            return new MessageResult(messageId,produceData.getTopic(),true);
        }else if(data instanceof Subscribe){
            Subscribe subscribe = (Subscribe)data;
            for(String topic: subscribe.getTopics()){
                serverConnectHolder.addConsumerChannel(topic, channel);
                serverConnectHolder.tryStartScanThread(topic);
            }
            return new MessageResult("common_id", subscribe.getTopics().get(0), true);
        }

        return NULL;
    }


}
