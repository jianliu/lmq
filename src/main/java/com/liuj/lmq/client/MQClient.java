package com.liuj.lmq.client;

import com.liuj.lsf.client.ClientHandler;
import com.liuj.lsf.openapi.AbstractOpenClient;

import java.net.ConnectException;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public class MQClient extends AbstractOpenClient {

    public MQClient(String host, int port, ClientHandler clientHandler) throws ConnectException{
        super(host, port, clientHandler);
    }


}
