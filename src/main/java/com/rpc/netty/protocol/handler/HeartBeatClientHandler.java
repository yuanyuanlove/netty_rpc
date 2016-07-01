package com.rpc.netty.protocol.handler;

import com.rpc.netty.protocol.model.Message;
import com.rpc.netty.protocol.model.MessageFactory;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 在一段时间内没有发起write请求的时候，就需要发起ping
 * Created by yuanyuan.pan on 2016/5/30.
 * this is 2016
 */
@ChannelHandler.Sharable
public class HeartBeatClientHandler extends ChannelDuplexHandler {
    private Logger logger = LoggerFactory.getLogger(HeartBeatClientHandler.class);
    public HeartBeatClientHandler() {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == IdleStateEvent.WRITER_IDLE_STATE_EVENT) {
            Message message = MessageFactory.buildPingMessage();
            ctx.writeAndFlush(message);
            logger.info("receive idle message, send ping message");
            return;
        }
        if (evt == IdleStateEvent.READER_IDLE_STATE_EVENT) {
            ctx.close();
        }
    }
}
