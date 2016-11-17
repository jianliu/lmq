package com.liuj.lmq.config;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public class ConsumerConfig extends BaseConfig{

    private int corePoolSize = 5;

    private int maxPoolSize = 200;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }
}
