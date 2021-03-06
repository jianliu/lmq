package com.liuj.lmq.job;

import com.liuj.lmq.bean.Subscribe;
import com.liuj.lmq.client.ClientManager;
import com.liuj.lmq.core.Message;
import com.liuj.lmq.bean.MessageResult;
import com.liuj.lmq.client.IConsumerClient;
import com.liuj.lmq.client.IMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public class MessageJob extends Thread {

    private final static Logger logger = LoggerFactory.getLogger(MessageJob.class);

    private ThreadPoolExecutor threadPool;

    private LinkedBlockingDeque<Message> messageQueue;

    private IConsumerClient consumerClient;

    private String topic;

    public MessageJob(ThreadPoolExecutor threadPool, LinkedBlockingDeque<Message> messageQueue, IConsumerClient client,
                      String topic) {
        this.threadPool = threadPool;
        this.messageQueue = messageQueue;
        this.consumerClient = client;
        this.topic = topic;
    }

    @Override
    public void run() {

//        this.consumerClient.getClient().sendMsg("");
        subscribeTopic(topic);

        for (; ; ) {
            List<IMessageListener> iMessageListeners = ClientManager.MESSAGES_LISTENS.get(this.topic);
            final Message message;
            try {
                message = messageQueue.take();
            } catch (InterruptedException e) {
                logger.error("consumer thread is Interrupted,exit now", e);
                break;
            }

            for (final IMessageListener messageListener : iMessageListeners) {

                threadPool.submit(new Runnable() {
                    public void run() {
                        try {
                            messageListener.onMessage(message);
                        } catch (Exception e) {
                            logger.error("处理消息失败:topic:{}", topic, e);
                        } finally {
                            consumerClient.getClient().sendResponse(new MessageResult(message.getMessageId(), message.getTopic(), true));
                        }

                    }
                });

            }
        }

        logger.warn("topic:{}处理线程已中断。", this.topic);
    }


    private void subscribeTopic(String topic) {
        Subscribe subscribe = new Subscribe();
        subscribe.setTopics(Arrays.asList(topic));
        try {
            //订阅服务
            MessageResult result = (MessageResult)this.consumerClient.getClient().sendMsgRet(subscribe);

            logger.info("订阅消息成功,{}", result.getHandled());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
