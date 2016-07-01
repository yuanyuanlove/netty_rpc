package com.rpc.netty.protocol.handler;

import com.rpc.netty.protocol.model.Message;
import com.rpc.netty.common.serialize.RpcSerialize;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Created by yuanyuan.pan on 2016/5/30.
 * this is 2016
 */
@ChannelHandler.Sharable
public class NettyMessageDecoder extends MessageToMessageDecoder<byte[]> {
    private RpcSerialize rpcSerialize;

    public RpcSerialize getRpcSerialize() {
        return rpcSerialize;
    }

    public void setRpcSerialize(RpcSerialize rpcSerialize) {
        this.rpcSerialize = rpcSerialize;
    }
    @Override
    protected void decode(ChannelHandlerContext ctx, byte[] bytes, List<Object> out) throws Exception {
        Message message = (Message) rpcSerialize.decode(bytes);
        out.add(message);
    }
}
