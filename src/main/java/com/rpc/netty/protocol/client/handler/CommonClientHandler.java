package com.rpc.netty.protocol.client.handler;

import com.rpc.netty.protocol.model.Message;
import com.rpc.netty.protocol.model.Response;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by yuanyuan on 2016/5/29.
 */
public class CommonClientHandler extends SimpleChannelInboundHandler<Message> {
    private Map<String, SettableFuture<Response>> settableFutureMap;
    private Logger logger = LoggerFactory.getLogger(CommonClientHandler.class);

    public Map<String, SettableFuture<Response>> getSettableFutureMap() {
        return settableFutureMap;
    }

    public void setSettableFutureMap(Map<String, SettableFuture<Response>> settableFutureMap) {
        this.settableFutureMap = settableFutureMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) {
        try {
            SettableFuture<Response> settableFuture = settableFutureMap.get(message.getId());
            settableFuture.set(message.getResponse());
        } catch (Exception e) {
            logger.error("channel read exception", e);
        }
    }

}
