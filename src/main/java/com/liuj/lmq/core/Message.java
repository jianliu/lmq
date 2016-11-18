package com.liuj.lmq.core;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public class Message implements Serializable {

    private final static long serialVersionUID = 1L;

    /**
     * 消息id，这个id会在后面消息成功解析处理后，作为处理成功的标识，同步的消息中间件中
     */
    private String messageId;

    /**
     * 消息所在的主题
     */
    private String topic;

    /**
     * 文本
     */
    private String text;

    /**
     * 消息的数据
     */
    private Object data;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
