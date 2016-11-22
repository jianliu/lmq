package com.liuj.lmq.server;

import com.liuj.lmq.bean.ProduceData;
import com.liuj.lmq.core.Message;
import com.liuj.lmq.utils.AssertUtil;
import com.liuj.lsf.core.AbstractLogger;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cdliujian1 on 2016/11/18.
 */
public class ServerMessageHolder extends AbstractLogger{

    private final ConcurrentMap<String, LinkedBlockingDeque<Message>> ALL_MESSAGES = new ConcurrentHashMap<String, LinkedBlockingDeque<Message>>();

    private AtomicInteger messageIdIdx = new AtomicInteger(0);

    public String add(ProduceData produceData){
        AssertUtil.notNull(produceData);
        LinkedBlockingDeque<Message> messages = ALL_MESSAGES.get(produceData.getTopic());
        if (messages == null) {
            messages = new LinkedBlockingDeque<Message>();
            ALL_MESSAGES.putIfAbsent(produceData.getTopic(), messages);
        }

        messages = ALL_MESSAGES.get(produceData.getTopic());
        Message message = new Message();
        message.setTopic(produceData.getTopic());
        message.setMessageId("int_"+ messageIdIdx.incrementAndGet());
        message.setText((String) produceData.getData());
        messages.add(message);
        logger.info("add message--id:{},text:{}", message.getTopic(), message.getText());

        return message.getMessageId();
    }

    public Set<String> getMessageTopics() {
        return ALL_MESSAGES.keySet();
    }

    /**
     * 获取topic的消息队列
     * @param topic
     * @return
     */
    public LinkedBlockingDeque<Message> getMessages(String topic){
       return ALL_MESSAGES.get(topic);
    }

    /**
     * 获取topic的消息队列，如果没有，则创建一个
     * @param topic
     * @return
     */
    public LinkedBlockingDeque<Message> sGetMessages(String topic){
        AssertUtil.notNull(topic);
        LinkedBlockingDeque<Message> messages = ALL_MESSAGES.get(topic);
        if (messages == null) {
            messages = new LinkedBlockingDeque<Message>();
            ALL_MESSAGES.putIfAbsent(topic, messages);
        }
        return ALL_MESSAGES.get(topic);
    }
}
