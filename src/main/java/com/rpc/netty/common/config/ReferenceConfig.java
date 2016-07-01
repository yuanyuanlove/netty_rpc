package com.rpc.netty.common.config;

import com.rpc.netty.common.serialize.RpcSerialize;
import com.rpc.netty.common.serialize.SerializeConfig;

/**
 * Created by yuanyuan.pan on 2016/5/30.
 * this is 2016
 */
public class ReferenceConfig {
    private String serviceName;
    private int timeOut = 30000;
    private int port = 9005;
    private int weight = 1;
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

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public RpcSerialize getRpcSerialize() {
        return rpcSerialize;
    }

    public void setRpcSerialize(RpcSerialize rpcSerialize) {
        this.rpcSerialize = rpcSerialize;
    }
}
