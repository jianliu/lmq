package com.liuj.lmq.server;

import com.liuj.lmq.utils.PropUtil;
import com.liuj.lsf.core.RequestHandle;
import com.liuj.lsf.openapi.AbstractOpenServer;
import com.liuj.lsf.server.ServerHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by cdliujian1 on 2016/11/18.
 */
public class MQServer extends AbstractOpenServer {

    private static Properties prop = PropUtil.load("/server.properties");

    public static void main(String[] args) throws Exception {

        ServerMessageHolder serverDataHolder = new ServerMessageHolder();
        ServerChannelMessageHandler serverConnectHolder = new ServerChannelMessageHandler(serverDataHolder);
        ServerRequestHandle serverRequestHandle = new ServerRequestHandle(serverDataHolder, serverConnectHolder);

        List<RequestHandle> requestHandleList = new ArrayList<RequestHandle>();
        requestHandleList.add(serverRequestHandle);

        ServerHandler mqServerHandler = new ServerHandler(requestHandleList);

        MQServer mqServer = new MQServer();
        mqServer.init(mqServerHandler,Integer.valueOf(prop.getProperty("port")));

        mqServer.startServer();
    }

}
