package com.rpc.netty.stub.client.impl;

import com.rpc.netty.protocol.client.NettyClient;
import com.rpc.netty.common.config.ReferenceConfig;
import com.rpc.netty.common.config.ServiceConfig;
import com.rpc.netty.protocol.model.Response;
import com.google.common.util.concurrent.SettableFuture;
import com.rpc.netty.stub.client.proxy.CglibReferenceProxy;
import com.rpc.netty.stub.client.Client;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * 简单客户端
 * Created by yuanyuan on 2016/5/29.
 */
public class SimpleReferenceClient<T> implements Client {
    private NettyClient nettyClient;
    private T targetProxy;
    private int weight;

    public SimpleReferenceClient(ServiceConfig serviceConfig, ReferenceConfig referenceConfig, Class targetInterface) {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(serviceConfig.getHost(), serviceConfig.getPort());
        this.nettyClient = new NettyClient(inetSocketAddress, referenceConfig.getRpcSerialize());
        CglibReferenceProxy cglibReferenceProxy = new CglibReferenceProxy(this, referenceConfig);
        targetProxy = (T) cglibReferenceProxy.createProxy(targetInterface);
        weight = referenceConfig.getWeight();
    }


    public NettyClient getNettyClient() {
        return nettyClient;
    }

    public void setNettyClient(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public void destroy() {
        nettyClient.destroy();
    }

    public T getTargetProxy() {
        return null;
    }

    @Override
    public SettableFuture<Response> invoke(Method method, List params) {
        return nettyClient.invoke(method, params);
    }

}
