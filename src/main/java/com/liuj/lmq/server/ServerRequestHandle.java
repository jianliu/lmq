package com.liuj.lmq.server;

import com.liuj.lmq.bean.MessageResult;
import com.liuj.lmq.bean.ProduceData;
import com.liuj.lmq.bean.Subscribe;
import com.liuj.lsf.core.AbstractLogger;
import com.liuj.lsf.core.RequestHandle;
import com.liuj.lsf.msg.MsgHeader;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by cdliujian1 on 2016/11/18.
 */
public class ServerRequestHandle extends AbstractLogger implements RequestHandle {

    private ServerMessageHolder serverDataHolder;

    private ServerChannelMessageHandler serverConnectHolder;

    public ServerRequestHandle(ServerMessageHolder serverDataHolder, ServerChannelMessageHandler serverConnectHolder) {
        this.serverDataHolder = serverDataHolder;
        this.serverConnectHolder = serverConnectHolder;
    }

    public void setServerDataHolder(ServerMessageHolder serverDataHolder) {
        this.serverDataHolder = serverDataHolder;
    }

    public void setServerConnectHolder(ServerChannelMessageHandler serverConnectHolder) {
        this.serverConnectHolder = serverConnectHolder;
    }

    public boolean canHandle(Object o) {
        return o instanceof ProduceData || o instanceof Subscribe;
    }

    public Object handleReq(MsgHeader msgHeader, Channel channel, Object o) {
        return handleRequest(o,channel);
    }

    private Object handleRequest(Object data, Channel channel) {
        if (data instanceof ProduceData) {
            ProduceData produceData = (ProduceData) data;
            String messageId = serverDataHolder.add(produceData);
            return new MessageResult(messageId,produceData.getTopic(),true);
        }else if(data instanceof Subscribe){
            Subscribe subscribe = (Subscribe)data;
            for(String topic: subscribe.getTopics()){

                CopyOnWriteArrayList<Channel> channels = serverConnectHolder.addConsumerChannel(topic, channel);
                synchronized (channels) {
                    channels.notifyAll();
                }

                serverConnectHolder.tryStartScanThread(topic);

                logger.info("find subscribe,topic:{},address:{}", topic, getRemoteAddress(channel));
            }
            return new MessageResult("common_id", subscribe.getTopics().get(0), true);
        }

        return NULL;
    }

    public String getRemoteAddress(Channel channel){
        InetSocketAddress address = (InetSocketAddress)channel.remoteAddress();
        return address.getHostName()+":"+address.getPort();
    }

}
