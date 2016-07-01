package com.rpc.netty.common.config;

import com.rpc.netty.common.serialize.RpcSerialize;

/**
 * Created by yuanyuan.pan on 2016/5/30.
 * this is 2016
 */
public class ServiceConfig {
    private String serviceName;
    private int timeOut = 3000;
    private String version = "1.0.0";
    private int port = 9005;
    private String host = "127.0.0.1";
    private RpcSerialize rpcSerialize;
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public RpcSerialize getRpcSerialize() {
        return rpcSerialize;
    }

    public void setRpcSerialize(RpcSerialize rpcSerialize) {
        this.rpcSerialize = rpcSerialize;
    }
}
