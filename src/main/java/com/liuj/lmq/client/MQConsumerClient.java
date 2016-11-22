package com.liuj.lmq.client;

import com.liuj.lmq.server.ServerManager;
import com.liuj.lmq.bean.MessageResult;
import com.liuj.lmq.bean.Subscribe;
import com.liuj.lmq.config.ConsumerConfig;
import com.liuj.lmq.core.Message;
import com.liuj.lmq.job.MessageJob;
import com.liuj.lsf.client.ClientHandler;
import com.liuj.lsf.core.ResponseListener;
import com.liuj.lsf.core.impl.LsfResponseClientListener;
import com.liuj.lsf.openapi.IClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
        List<ResponseListener> responseListeners = new ArrayList<ResponseListener>();
        responseListeners.add(new LsfResponseClientListener());
        responseListeners.add(new MQResponseListener());

        ClientHandler clientHandler = new ClientHandler(responseListeners);
        client = new MQClient(this.server, this.serverPort, clientHandler);
        client.setTimeout(this.timeoutInMills);
    }

    public void registerListener(ConsumerConfig consumerConfig, IMessageListener messageListener) {
        String topic = consumerConfig.getTopic();
        List<IMessageListener> messageListenerList = ClientManager.MESSAGES_LISTENS.get(topic);
        if (messageListenerList == null) {
            messageListenerList = new CopyOnWriteArrayList<IMessageListener>();
            ClientManager.MESSAGES_LISTENS.put(topic, messageListenerList);
        }
        messageListenerList.add(messageListener);

        ThreadPoolExecutor threadPoolExecutor = ClientManager.TOPIC_THREAD_POOL.get(topic);
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutor(consumerConfig.getCorePoolSize(), consumerConfig.getMaxPoolSize(), 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100));
            ClientManager.TOPIC_THREAD_POOL.putIfAbsent(topic, threadPoolExecutor);
        }
    }

    public void reconnect() {
        lock.lock();
        try {
            if (!client.isActive()) {
                client.reconnect();
            }
        } finally {
            lock.unlock();
        }
    }

    public void startWork() {
        Set<String> topics = ClientManager.MESSAGES_LISTENS.keySet();
        Subscribe subscribe = new Subscribe();
        subscribe.setTopics(new ArrayList<String>(topics));
        try {
            //订阅服务
            MessageResult result = (MessageResult)this.client.sendMsgRet(subscribe);

            logger.info("订阅消息成功,{}", result.getHandled());
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String topic : topics) {
            LinkedBlockingDeque<Message> messageQueue = ServerManager.checkMessageTopic(topic);
            MessageJob job = new MessageJob(ClientManager.TOPIC_THREAD_POOL.get(topic), messageQueue, this, topic);
            job.start();
        }
        logger.warn("启动各业务线程池成功");
    }

    public IClient getClient() {
        return client;
    }
}
