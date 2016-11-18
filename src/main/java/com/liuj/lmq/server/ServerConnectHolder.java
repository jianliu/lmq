package com.liuj.lmq.server;

import com.liuj.lmq.core.Message;
import com.liuj.lsf.msg.MsgHeader;
import com.liuj.lsf.msg.ResponseMsg;
import io.netty.channel.Channel;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by cdliujian1 on 2016/11/18.
 */
public class ServerConnectHolder {

    private final ConcurrentMap<String, CopyOnWriteArrayList<Channel>> TOPIC_CHANNELS = new ConcurrentHashMap<String, CopyOnWriteArrayList<Channel>>();

    private ServerMessageHolder serverMessageHolder;

    private ReentrantLock topicThreadLock = new ReentrantLock();

    private Set<String> topicThreadsOn = new HashSet<String>();

    public ServerConnectHolder(ServerMessageHolder serverMessageHolder) {
        this.serverMessageHolder = serverMessageHolder;
    }

    public void setServerMessageHolder(ServerMessageHolder serverMessageHolder) {
        this.serverMessageHolder = serverMessageHolder;
    }

    public synchronized void addConsumerChannel(String topic, Channel channel) {
        CopyOnWriteArrayList<Channel> channels = TOPIC_CHANNELS.get(topic);
        if (channels == null) {
            channels = new CopyOnWriteArrayList<Channel>();
            TOPIC_CHANNELS.putIfAbsent(topic, channels);
        }
        channels.add(channel);
    }

    //每个topic一个线程
    public void tryStartScanThread(final String topic) {
        topicThreadLock.lock();
        try {
            if (topicThreadsOn.contains(topic)) {
                return;
            }
        } finally {
            topicThreadLock.unlock();
        }
        new Thread(new Runnable() {
            public void run() {
                for (; ; ) {
                    LinkedBlockingDeque<Message> messages = serverMessageHolder.getMessages(topic);
                    try {
                        Message message = messages.take();
                        CopyOnWriteArrayList<Channel> channels = TOPIC_CHANNELS.get(topic);
                        for (Channel channel : channels) {
                            sendResponse(channel, message);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    protected void sendResponse(Channel channel, Object messageResult) {
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setClz(messageResult.getClass().getCanonicalName());
        msgHeader.setMsgType(2);
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setReceiveTime(System.currentTimeMillis());
        responseMsg.setResponse(messageResult);
        responseMsg.setMsgHeader(msgHeader);
        channel.writeAndFlush(responseMsg, channel.voidPromise());
    }

}
