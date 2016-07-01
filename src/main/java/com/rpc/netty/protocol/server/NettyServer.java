package com.rpc.netty.protocol.server;

import com.rpc.netty.common.serialize.RpcSerialize;
import com.rpc.netty.protocol.threadpool.RpcThreadPoolFactory;
import com.rpc.netty.protocol.server.handler.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * netty client
 * Created by yuanyuan on 2016/5/28.
 */
public class NettyServer {
    private int port;
    private ChannelFuture channelFuture;
    private Object objectTarget;
    private Class targetInterface;
    private RpcSerialize rpcSerialize;

    private Logger logger = LoggerFactory.getLogger(NettyServer.class);

    /**
     *
     * @param port
     * @param objectTarget
     */
    public NettyServer(int port, Object objectTarget, RpcSerialize rpcSerialize) {
        this.port = port;
        this.objectTarget = objectTarget;
        this.rpcSerialize = rpcSerialize;
    }

    public void init() {
        targetInterface = objectTarget.getClass().getInterfaces()[0];
        RpcThreadPoolFactory rpcThreadPoolFactory = RpcThreadPoolFactory.getFactory();
        ServerChannelInitializer serverChannelInitializer = new ServerChannelInitializer();
        serverChannelInitializer.setRpcSerialize(rpcSerialize);
        serverChannelInitializer.setObjectTarget(objectTarget);
        serverChannelInitializer.setTargetInterface(targetInterface);
        ServerBootstrap serverBootstrap = new ServerBootstrap().group(rpcThreadPoolFactory.getBossEventLoopGroup(), rpcThreadPoolFactory.getWorkerEventLoopGroup())
                .channel(NioServerSocketChannel.class).childOption(ChannelOption.SO_BACKLOG, 1000)
                .childHandler(serverChannelInitializer);
        try {
            channelFuture = serverBootstrap.bind(port).sync();
        } catch (InterruptedException e) {
            logger.error("server bind exception", e);
            throw new RuntimeException("端口无法绑定");
        }
    }

    public void destroy() {
        try {
            channelFuture.channel().close().sync();
        } catch (InterruptedException e) {
            logger.error("channel close exception", e);
        }
    }

    public Object getObjectTarget() {
        return objectTarget;
    }

    public void setObjectTarget(Object objectTarget) {
        this.objectTarget = objectTarget;
    }

    public Class getTargetInterface() {
        return targetInterface;
    }

    public void setTargetInterface(Class targetInterface) {
        this.targetInterface = targetInterface;
    }
}
