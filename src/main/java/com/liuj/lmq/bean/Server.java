package com.liuj.lmq.bean;

/**
 * 服务server地址pojo对象
 * Created by cdliujian1 on 2016/11/23.
 */
public class Server {

    /**
     * server 中心地址
     */
    private String index;

    /**
     * 端口
     */
    private int port;

    public Server() {
    }

    public Server(String index, int port) {
        this.index = index;
        this.port = port;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
