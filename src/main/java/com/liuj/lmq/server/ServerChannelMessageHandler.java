package com.liuj.lmq.server;

import com.liuj.lmq.core.Message;
import com.liuj.lsf.core.AbstractLogger;
import com.liuj.lsf.msg.MsgHeader;
import com.liuj.lsf.msg.ResponseMsg;
import io.netty.channel.Channel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by cdliujian1 on 2016/11/18.
 * 服务端server扫描topic列表，分发数据到comsumer
 */
public class ServerChannelMessageHandler extends AbstractLogger {

    private final ConcurrentMap<String, CopyOnWriteArrayList<Channel>> TOPIC_CHANNELS = new ConcurrentHashMap<String, CopyOnWriteArrayList<Channel>>();

    private ServerMessageHolder serverMessageHolder;

    private ReentrantLock topicThreadLock = new ReentrantLock();

    private Set<String> topicThreadsOn = new HashSet<String>();

    public ServerChannelMessageHandler(ServerMessageHolder serverMessageHolder) {
        this.serverMessageHolder = serverMessageHolder;
    }

    public void setServerMessageHolder(ServerMessageHolder serverMessageHolder) {
        this.serverMessageHolder = serverMessageHolder;
    }

    public synchronized CopyOnWriteArrayList<Channel> addConsumerChannel(String topic, Channel channel) {
        CopyOnWriteArrayList<Channel> channels = TOPIC_CHANNELS.get(topic);
        if (channels == null) {
            channels = new CopyOnWriteArrayList<Channel>();
            TOPIC_CHANNELS.putIfAbsent(topic, channels);
        }
        channels.add(channel);
        return channels;
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
                topicThreadsOn.add(topic);
                logger.info("start scan thread on topic:{}", topic);

                for (; ; ) {

                    //退出线程
                    if (!TOPIC_CHANNELS.containsKey(topic)) {
                        return;
                    }

                    LinkedBlockingDeque<Message> messages = serverMessageHolder.sGetMessages(topic);
                    logger.info("about message,topic:{},size:{}", topic, messages.size());

                    try {
                        Message message = messages.take();
                        CopyOnWriteArrayList<Channel> channels = TOPIC_CHANNELS.get(topic);

                        synchronized (channels) {
                            if (channels.size() == 0) {
                                channels.wait();
                            }
                        }

                        if (!sendResponse(channels, message)) {
                            //消息处理失败,重新仍如队列
                            messages.addFirst(message);
                            logger.warn("消息处理失败，重新入队列.");
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

    /**
     * 从channel列表获取一个channel，处理消息
     *
     * @param channels
     * @param message
     * @return
     */
    protected boolean sendResponse(CopyOnWriteArrayList<Channel> channels, Message message) {

        for (Channel channel : channels) {
            try {
                if (channel.isOpen() && channel.isActive()) {
                    sendResponse0(channel, message);
                    logger.info("send data to consumer,topic:{},text:{}", message.getTopic(), message.getText());
                    return true;
                }
            } catch (Exception ignore) {

            }
            if (!channel.isOpen() || !channel.isActive()) {
                logger.info("channel is not active,close now...");
                channels.remove(channel);
                channel.close();
            }
        }
        return false;
    }

    protected void sendResponse0(Channel channel, Object messageResult) {
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setClz(messageResult.getClass().getCanonicalName());
        msgHeader.setMsgType(2);
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setReceiveTime(System.currentTimeMillis());
        responseMsg.setResponse(messageResult);
        responseMsg.setMsgHeader(msgHeader);
        channel.writeAndFlush(responseMsg/*, channel.voidPromise()*/);
    }

}
