package com.liuj.lmq.client;

import com.liuj.lmq.config.ConsumerConfig;

/**
 * Created by cdliujian1 on 2016/11/23.
 */
public class Consumer {

    private String topic;

    private int maxPoolSize;

    private int corePoolSize;

    private IConsumerClient consumerClient;

    private IMessageListener listener;

    public void registerListener(){
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setTopic(topic);
        consumerClient.registerListener(consumerConfig,listener, true);
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public IConsumerClient getConsumerClient() {
        return consumerClient;
    }

    public void setConsumerClient(IConsumerClient consumerClient) {
        this.consumerClient = consumerClient;
    }

    public IMessageListener getListener() {
        return listener;
    }

    public void setListener(IMessageListener listener) {
        this.listener = listener;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }
}
