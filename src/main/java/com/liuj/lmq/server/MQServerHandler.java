package com.liuj.lmq.server;

import com.liuj.lsf.msg.RequestMsg;
import com.liuj.lsf.msg.ResponseMsg;
import com.liuj.lsf.server.AbstractServerHandler;
import com.liuj.lsf.server.ServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cdliujian1 on 2016/11/18.
 */
@ChannelHandler.Sharable
public class MQServerHandler extends AbstractServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private RequestHandle requestHandle;

    public void setRequestHandle(RequestHandle requestHandle) {
        this.requestHandle = requestHandle;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        Channel channel = ctx.channel();
        if (msg instanceof RequestMsg) {
//            RequestMsg requestMsg = (RequestMsg) msg;
            Object requestData = ((RequestMsg) msg).getConsumerBean();
            logger.info("find RequestMsg ");
            ResponseMsg responseMsg = new ResponseMsg();
            responseMsg.setReceiveTime(System.currentTimeMillis());

            Object result = requestHandle.handleRequest(requestData, channel);

            if (result != RequestHandle.NULL) {
                sendResponse(channel, result);
            }


        } else if (msg instanceof ResponseMsg) {
            //receive the callback ResponseMessage
            ResponseMsg responseMsg = (ResponseMsg) msg;

            //find the transport

        }

    }


}
