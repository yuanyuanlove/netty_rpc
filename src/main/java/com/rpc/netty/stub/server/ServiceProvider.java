package com.rpc.netty.stub.server;

import com.rpc.netty.common.config.ServiceConfig;
import com.rpc.netty.common.zookeeper.ServiceDiscovery;
import com.rpc.netty.common.zookeeper.impl.ZkServiceDiscoveryImpl;
import com.rpc.netty.protocol.server.NettyServer;
import com.rpc.netty.protocol.threadpool.RpcThreadPoolFactory;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.curator.framework.CuratorFramework;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by yuanyuan.pan on 2016/6/1.
 * this is 2016
 */
public class ServiceProvider implements Server{
    private NettyServer nettyServer;
    private ServiceConfig serviceConfig;
    private ServiceDiscovery serviceDiscovery;
    public ServiceProvider(ServiceConfig serviceConfig, CuratorFramework client, Object targetObject) {
        this.serviceConfig = serviceConfig;
        nettyServer = new NettyServer(serviceConfig.getPort(), targetObject, serviceConfig.getRpcSerialize());
        serviceDiscovery = new ZkServiceDiscoveryImpl(client);
        serviceDiscovery.registryProvider(serviceConfig);
    }

    public NettyServer getNettyServer() {
        return nettyServer;
    }

    public void setNettyServer(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    public ServiceConfig getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    @Override
    public void destroy() {
        nettyServer.destroy();
    }

    @Override
    public void bind() {
        nettyServer.init();
    }

}
