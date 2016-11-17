package com.liuj.lmq.client;

import com.liuj.lmq.bean.ProduceData;
import com.liuj.lmq.config.ProducerConfig;
import com.liuj.lmq.utils.AssertUtil;
import com.liuj.lsf.client.ClientHandler;
import com.liuj.lsf.openapi.IClient;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public class Producer implements IProducer{

    private String server;

    private int serverPort;

    private int timeoutInMills;

    private ProducerConfig producerConfig;

    private IClient client;

    private ReentrantReadWriteLock mainLock = new ReentrantReadWriteLock();

    private ReentrantReadWriteLock.ReadLock readLock = mainLock.readLock();

    private ReentrantReadWriteLock.WriteLock writeLock = mainLock.writeLock();

    public Producer(String server, int serverPort, int timeoutInMills, ProducerConfig producerConfig) {
        this.server = server;
        this.serverPort = serverPort;
        this.timeoutInMills = timeoutInMills;
        this.producerConfig = producerConfig;
        client = new MQClient(this.server, this.serverPort, new ClientHandler());
        client.setTimeout(this.timeoutInMills);
    }

    public void reconnect(){
        writeLock.lock();
        try {
            client = new MQClient(this.server, this.serverPort, new ClientHandler());
            client.setTimeout(this.timeoutInMills);
        }finally {
            writeLock.unlock();
        }
    }

    public void publish(Object data) {
        AssertUtil.notNull(data);
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

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getTimeoutInMills() {
        return timeoutInMills;
    }

    public void setTimeoutInMills(int timeoutInMills) {
        this.timeoutInMills = timeoutInMills;
    }
}
