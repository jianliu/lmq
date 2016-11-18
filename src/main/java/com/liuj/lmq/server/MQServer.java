package com.liuj.lmq.server;

import com.liuj.lmq.utils.PropUtil;
import com.liuj.lsf.openapi.AbstractOpenServer;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by cdliujian1 on 2016/11/18.
 */
public class MQServer extends AbstractOpenServer {

    private static Properties prop = PropUtil.load("/server.properties");

    public static void main(String[] args) throws Exception {

        ServerMessageHolder serverDataHolder = new ServerMessageHolder();

        ServerConnectHolder serverConnectHolder = new ServerConnectHolder(serverDataHolder);

        ServerRequestHandle serverRequestHandle = new ServerRequestHandle(serverDataHolder, serverConnectHolder);

        MQServerHandler mqServerHandler = new MQServerHandler();
        mqServerHandler.setRequestHandle(serverRequestHandle);

        MQServer mqServer = new MQServer();
        mqServer.init(mqServerHandler,Integer.valueOf(prop.getProperty("port")));
        mqServer.startServer();
    }

}
