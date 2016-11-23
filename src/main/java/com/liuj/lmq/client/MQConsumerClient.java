package com.liuj.lmq.client;

import com.liuj.lmq.bean.Server;
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

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public class MQConsumerClient implements IConsumerClient {

    private final static Logger logger = LoggerFactory.getLogger(MQConsumerClient.class);

    private Server server;

    private int timeoutInMills = 5000;

    private ReentrantLock lock = new ReentrantLock();

    private IClient client;

    public MQConsumerClient() {
    }

    public MQConsumerClient(Server server, int timeoutInMills) {
        this.server = server;
        this.timeoutInMills = timeoutInMills;
        init();
    }

    public void init(){
        List<ResponseListener> responseListeners = new ArrayList<ResponseListener>();
        responseListeners.add(new LsfResponseClientListener());
        responseListeners.add(new MQResponseListener());

        ClientHandler clientHandler = new ClientHandler(responseListeners);
        try {
            client = new MQClient(this.server.getIndex(), this.server.getPort(), clientHandler);
            client.setTimeout(this.timeoutInMills);
        }catch (ConnectException ce){
            logger.error("连接到server端失败");
        }
    }

    public void registerListener(ConsumerConfig consumerConfig, IMessageListener messageListener, boolean openThread) {
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

        if(openThread) {
            //注册时启动线程
            LinkedBlockingDeque<Message> messageQueue = ServerManager.checkMessageTopic(topic);
            MessageJob job = new MessageJob(ClientManager.TOPIC_THREAD_POOL.get(topic), messageQueue, this, topic);
            job.start();
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
        subscribeTopic(topics);

        for (String topic : topics) {
            LinkedBlockingDeque<Message> messageQueue = ServerManager.checkMessageTopic(topic);
            MessageJob job = new MessageJob(ClientManager.TOPIC_THREAD_POOL.get(topic), messageQueue, this, topic);
            job.start();
        }
        logger.warn("启动各业务线程池成功");
    }

    private void subscribeTopic(Set<String> topics) {
        Subscribe subscribe = new Subscribe();
        subscribe.setTopics(new ArrayList<String>(topics));
        try {
            //订阅服务
            MessageResult result = (MessageResult)this.client.sendMsgRet(subscribe);

            logger.info("订阅消息成功,{}", result.getHandled());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IClient getClient() {
        return client;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setTimeoutInMills(int timeoutInMills) {
        this.timeoutInMills = timeoutInMills;
    }
}
