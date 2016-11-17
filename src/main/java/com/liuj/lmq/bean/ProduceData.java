package com.liuj.lmq.bean;

import java.io.Serializable;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public class ProduceData implements Serializable{
    
    private final static long serialVersionUID = 1L;
    
    private String topic;
    
    private boolean multi;
    
    private Object data;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
