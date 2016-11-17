package com.liuj.lmq.config;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public class BaseConfig {

    private String topic;

    private boolean multi;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }
}
