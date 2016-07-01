package com.rpc.netty.protocol.threadpool;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yuanyuan.pan on 2016/6/16.
 * this is 2016
 */
public class RpcThreadPoolFactory {
    private EventLoopGroup bossEventLoopGroup;
    private EventLoopGroup workerEventLoopGroup;
    private ExecutorService businessExecutor;

    public EventLoopGroup getBossEventLoopGroup() {
        return bossEventLoopGroup;
    }

    public void setBossEventLoopGroup(EventLoopGroup bossEventLoopGroup) {
        this.bossEventLoopGroup = bossEventLoopGroup;
    }

    public EventLoopGroup getWorkerEventLoopGroup() {
        return workerEventLoopGroup;
    }

    public void setWorkerEventLoopGroup(EventLoopGroup workerEventLoopGroup) {
        this.workerEventLoopGroup = workerEventLoopGroup;
    }

    void destroy() {
        bossEventLoopGroup.shutdownGracefully();
        workerEventLoopGroup.shutdownGracefully();
        businessExecutor.shutdownNow();
    }

    public ExecutorService getBusinessExecutor() {
        return businessExecutor;
    }

    public void setBusinessExecutor(ExecutorService businessExecutor) {
        this.businessExecutor = businessExecutor;
    }

    private RpcThreadPoolFactory() {
        bossEventLoopGroup = new NioEventLoopGroup(2, new DefaultThreadFactory("netty_nio_boos_thread_pool", true));
        workerEventLoopGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2, new DefaultThreadFactory("netty_nio_worker_thread_pool", true));
        businessExecutor = Executors.newFixedThreadPool(100, new DefaultThreadFactory("rpc_business_thread_pool", true));
    }

    public static RpcThreadPoolFactory getFactory() {
        return RpcThreadPoolFactoryHolder.rpcThreadPoolFactory;
    }

    private static class RpcThreadPoolFactoryHolder {
        private static RpcThreadPoolFactory rpcThreadPoolFactory = new RpcThreadPoolFactory();
    }
}
