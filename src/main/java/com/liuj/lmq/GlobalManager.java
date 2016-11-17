package com.liuj.lmq;

import com.liuj.lmq.bean.Message;
import com.liuj.lmq.client.IMessageListener;
import com.liuj.lmq.utils.AssertUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public class GlobalManager {

    /**
     * topic，机器队列
     */
    public final static ConcurrentMap<String, LinkedBlockingDeque<Message>> ALL_MESSAGES = new ConcurrentHashMap<String, LinkedBlockingDeque<Message>>();


    /**
     *
     */
    public final static ConcurrentMap<String, List<IMessageListener>> MESSAGES_LISTENS = new ConcurrentHashMap<String, List<IMessageListener>>();


    public final static ConcurrentHashMap<String,ThreadPoolExecutor> TOPIC_THREAD_POOL = new ConcurrentHashMap<String, ThreadPoolExecutor>();

    /**
     * 订阅主题
     * ALL_MESSAGES同时生成对应的列表
     * @param topic
     */
    public static void SubTopic(String topic) {
        AssertUtil.notNull(topic);
        LinkedBlockingDeque<Message> messages = ALL_MESSAGES.get(topic);
        if (messages == null) {
            messages = new LinkedBlockingDeque<Message>();
            ALL_MESSAGES.putIfAbsent(topic, messages);
        }
    }

}
