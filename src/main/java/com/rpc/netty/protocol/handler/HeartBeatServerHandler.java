package com.rpc.netty.protocol.handler;

import com.rpc.netty.protocol.model.Message;
import com.rpc.netty.protocol.model.MessageFactory;
import com.rpc.netty.protocol.model.MessageType;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by yuanyuan.pan on 2016/5/30.
 * this is 2016
 */
@ChannelHandler.Sharable
public class HeartBeatServerHandler extends SimpleChannelInboundHandler<Message> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        if(msg.getMessageType() == MessageType.ping) {
            Message message = MessageFactory.buildPongMessage();
            ctx.writeAndFlush(message);
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
