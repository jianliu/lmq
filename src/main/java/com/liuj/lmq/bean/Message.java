package com.liuj.lmq.bean;

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

    private String topic;

    /**
     * 消息列表
     */
    private List<Object> messages;

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

    public List<Object> getMessages() {
        return messages;
    }

    public void setMessages(List<Object> messages) {
        this.messages = messages;
    }
}
