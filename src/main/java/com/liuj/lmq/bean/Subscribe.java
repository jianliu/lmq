package com.liuj.lmq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 订阅主题列表
 * Created by cdliujian1 on 2016/11/17.
 */
public class Subscribe implements Serializable {

    private final static long serialVersionUID = 1L;


    private List<String> topics;

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}
