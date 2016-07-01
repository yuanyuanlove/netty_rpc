package com.rpc.netty.protocol.server.handler;

import com.rpc.netty.protocol.handler.HeartBeatServerHandler;
import com.rpc.netty.protocol.handler.NettyMessageDecoder;
import com.rpc.netty.protocol.handler.NettyMessageEncoder;
import com.rpc.netty.common.serialize.RpcSerialize;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.*;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by yuanyuan on 2016/5/29.
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private Object objectTarget;
    private Class targetInterface;
    private RpcSerialize rpcSerialize;

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

    public RpcSerialize getRpcSerialize() {
        return rpcSerialize;
    }

    public void setRpcSerialize(RpcSerialize rpcSerialize) {
        this.rpcSerialize = rpcSerialize;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        CommonServerHandler commonServerHandler = new CommonServerHandler();
        commonServerHandler.setObjectTarget(objectTarget);
        commonServerHandler.setTargetInterface(targetInterface);
        NettyMessageDecoder nettyMessageDecoder = new NettyMessageDecoder();
        nettyMessageDecoder.setRpcSerialize(rpcSerialize);
        NettyMessageEncoder nettyMessageEncoder = new NettyMessageEncoder();
        nettyMessageEncoder.setRpcSerialize(rpcSerialize);
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
                .addLast(new LengthFieldPrepender(2))
                .addLast("bytesDecoder", new ByteArrayDecoder())
                .addLast("bytesEncoder", new ByteArrayEncoder())
                .addLast("nettyDecoder", nettyMessageDecoder)
                .addLast("nettyEncoder", nettyMessageEncoder)
                .addLast("logHandler", new LoggingHandler(LogLevel.INFO))
                .addLast("pongHandler", new HeartBeatServerHandler())
                .addLast(commonServerHandler);
    }
}
