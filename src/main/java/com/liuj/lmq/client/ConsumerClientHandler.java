package com.liuj.lmq.client;

import com.liuj.lmq.GlobalManager;
import com.liuj.lmq.bean.Message;
import com.liuj.lsf.client.ClientHandler;
import com.liuj.lsf.msg.ResponseMsg;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public class ConsumerClientHandler extends ClientHandler {

    private final static Logger logger = LoggerFactory.getLogger(ConsumerClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof ResponseMsg) {
            ResponseMsg responseMsg = (ResponseMsg) msg;
            Object data = responseMsg.getResponse();
            if (data instanceof Message) {
                Message message = (Message) data;
                if (!GlobalManager.ALL_MESSAGES.containsKey(message.getTopic())) {
                    return;
                }
                LinkedBlockingDeque<Message> messageQueue = GlobalManager.ALL_MESSAGES.get(message.getTopic());
                logger.debug("logging queue size,topic:{}, size:{}", message.getTopic(), messageQueue.size());
                messageQueue.add(message);
            }
        }
    }
}
