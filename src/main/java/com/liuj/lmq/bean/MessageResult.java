package com.liuj.lmq.bean;

import java.io.Serializable;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public class MessageResult implements Serializable {

    private final static long serialVersionUID = 1L;

    /**
     * 消息id，这个id会在后面消息成功解析处理后，作为处理成功的标识，同步的消息中间件中
     */
    private String messageId;

    /**
     * 主题
     */
    private String topic;

    /**
     * 消息是否被处理
     */
    private Boolean handled;

    public MessageResult(String messageId, String topic, Boolean handled) {
        this.messageId = messageId;
        this.topic = topic;
        this.handled = handled;
    }

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

    public Boolean getHandled() {
        return handled;
    }

    public void setHandled(Boolean handled) {
        this.handled = handled;
    }
}
