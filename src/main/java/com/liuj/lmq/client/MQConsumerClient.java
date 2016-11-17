package com.liuj.lmq.client;

import com.liuj.lmq.GlobalManager;
import com.liuj.lmq.bean.Message;
import com.liuj.lmq.bean.MessageResult;
import com.liuj.lmq.config.ConsumerConfig;
import com.liuj.lmq.job.MessageJob;
import com.liuj.lsf.client.ClientHandler;
import com.liuj.lsf.openapi.IClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public class MQConsumerClient implements IConsumerClient {

    private final static Logger logger = LoggerFactory.getLogger(MQConsumerClient.class);

    private String server;

    private int serverPort;

    private int timeoutInMills;

    private ReentrantLock lock = new ReentrantLock();

    private IClient client;

    public MQConsumerClient(String host, int port, int timeoutInMills) {
        this.server = host;
        this.serverPort = port;
        this.timeoutInMills = timeoutInMills;
        client = new MQClient(this.server, this.serverPort, new ConsumerClientHandler());
        client.setTimeout(this.timeoutInMills);
    }

    public void registerListener(ConsumerConfig consumerConfig, IMessageListener messageListener) {
        String topic = consumerConfig.getTopic();
        List<IMessageListener> messageListenerList = GlobalManager.MESSAGES_LISTENS.get(topic);
        if (messageListenerList == null) {
            messageListenerList = new CopyOnWriteArrayList<IMessageListener>();
            GlobalManager.MESSAGES_LISTENS.put(topic, messageListenerList);
        }
        messageListenerList.add(messageListener);

        ThreadPoolExecutor threadPoolExecutor = GlobalManager.TOPIC_THREAD_POOL.get(topic);
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutor(consumerConfig.getCorePoolSize(), consumerConfig.getMaxPoolSize(), 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100));
            GlobalManager.TOPIC_THREAD_POOL.putIfAbsent(topic, threadPoolExecutor);
        }
    }

    public void reconnect() {
        lock.lock();
        try {
            client = new MQClient(this.server, this.serverPort, new ClientHandler());
            client.setTimeout(this.timeoutInMills);
        } finally {
            lock.unlock();
        }
    }

    public void startWork() {
        Set<String> topics = GlobalManager.ALL_MESSAGES.keySet();
        for (String topic : topics) {
            LinkedBlockingDeque<Message> messageQueue = GlobalManager.ALL_MESSAGES.get(topic);
            MessageJob job = new MessageJob(GlobalManager.TOPIC_THREAD_POOL.get(topic),messageQueue, this, topic);
            job.start();
        }
        logger.warn("启动各业务线程池成功");
    }

    public IClient getClient() {
        return client;
    }
}
