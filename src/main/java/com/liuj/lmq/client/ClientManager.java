package com.liuj.lmq.client;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by cdliujian1 on 2016/11/22.
 */
public class ClientManager {

    /**
     * topic的消息监听器
     */
    public final static ConcurrentMap<String, List<IMessageListener>> MESSAGES_LISTENS = new ConcurrentHashMap<String, List<IMessageListener>>();


    public final static ConcurrentHashMap<String, ThreadPoolExecutor> TOPIC_THREAD_POOL = new ConcurrentHashMap<String, ThreadPoolExecutor>();


}
