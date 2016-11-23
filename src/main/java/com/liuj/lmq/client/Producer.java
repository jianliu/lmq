package com.liuj.lmq.client;

import com.liuj.lmq.bean.ProduceData;
import com.liuj.lmq.config.ProducerConfig;
import com.liuj.lmq.utils.AssertUtil;
import com.liuj.lsf.client.ClientHandler;
import com.liuj.lsf.core.AbstractLogger;
import com.liuj.lsf.core.ResponseListener;
import com.liuj.lsf.core.impl.LsfResponseClientListener;
import com.liuj.lsf.openapi.IClient;
import com.liuj.lmq.bean.Server;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public class Producer extends AbstractLogger implements IProducer{

    private Server server;

    private int timeoutInMills = 3000;

    private ProducerConfig producerConfig;

    private IClient client;

    private ReentrantReadWriteLock mainLock = new ReentrantReadWriteLock();

    private ReentrantReadWriteLock.ReadLock readLock = mainLock.readLock();

    private ReentrantReadWriteLock.WriteLock writeLock = mainLock.writeLock();

    public Producer() {
    }

    public Producer(Server server, int timeoutInMills, ProducerConfig producerConfig) {
        this.server = server;
        this.timeoutInMills = timeoutInMills;
        this.producerConfig = producerConfig;
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
            logger.warn("连接server端失败");
        }
    }

    public boolean reconnect(){
        writeLock.lock();
        try {
            client = new MQClient(this.server.getIndex(), this.server.getPort(), new ClientHandler());
            client.setTimeout(this.timeoutInMills);
            return true;
        }catch (ConnectException ce){
            logger.warn("连接server端失败");
        }finally {
            writeLock.unlock();
        }
        return false;
    }

    public void publish(Object data) {
        AssertUtil.notNull(data);
        if(client == null ){
            logger.warn("尝试连接到server");
            boolean success = reconnect();
            if(!success){
                logger.error("连接到server端失败，请检测server地址是否可达");
                throw new RuntimeException("连接到server端失败");
            }
        }

        readLock.lock();
        try {
            client.sendMsg(buildProduceData(data));
        }finally {
            readLock.unlock();
        }
    }

    public void addMessage(Object data) {
        publish(data);
    }

    private ProduceData buildProduceData(Object originData){
        ProduceData produceData = new ProduceData();
        produceData.setData(originData);
        produceData.setTopic(this.producerConfig.getTopic());
        produceData.setMulti(this.producerConfig.isMulti());
        return produceData;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public int getTimeoutInMills() {
        return timeoutInMills;
    }

    public void setTimeoutInMills(int timeoutInMills) {
        this.timeoutInMills = timeoutInMills;
    }

    public void setProducerConfig(ProducerConfig producerConfig) {
        this.producerConfig = producerConfig;
    }
}
