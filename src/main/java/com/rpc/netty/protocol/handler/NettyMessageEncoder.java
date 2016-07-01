package com.rpc.netty.protocol.handler;

import com.rpc.netty.protocol.model.Message;
import com.rpc.netty.common.serialize.RpcSerialize;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by yuanyuan.pan on 2016/5/30.
 * this is 2016
 */
@ChannelHandler.Sharable
public class NettyMessageEncoder extends MessageToMessageEncoder<Message> {
    private RpcSerialize rpcSerialize;

    public RpcSerialize getRpcSerialize() {
        return rpcSerialize;
    }

    public void setRpcSerialize(RpcSerialize rpcSerialize) {
        this.rpcSerialize = rpcSerialize;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        byte[] bytes = rpcSerialize.encode(msg);
        out.add(bytes);
    }
}
