package com.rpc.netty.protocol.client;
import com.rpc.netty.protocol.client.handler.CommonClientHandler;
import com.rpc.netty.protocol.handler.HeartBeatClientHandler;
import com.rpc.netty.protocol.handler.NettyMessageDecoder;
import com.rpc.netty.protocol.handler.NettyMessageEncoder;
import com.rpc.netty.protocol.model.Message;
import com.rpc.netty.protocol.model.MessageFactory;
import com.rpc.netty.protocol.model.Request;
import com.rpc.netty.protocol.model.Response;
import com.rpc.netty.common.serialize.RpcSerialize;
import com.rpc.netty.protocol.threadpool.RpcThreadPoolFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 客户端
 * Created by yuanyuan on 2016/5/29.
 */
public class NettyClient {
    private InetSocketAddress inetSocketAddress;
    private AtomicBoolean start = new AtomicBoolean(false);
    private ChannelFuture channelFuture;
    private Bootstrap bootstrap;
    private RpcSerialize rpcSerialize;
    private Map<String, SettableFuture<Response>> settableFutureMap = Maps.newConcurrentMap();
    private Logger logger = LoggerFactory.getLogger(NettyClient.class);

    public NettyClient(InetSocketAddress inetSocketAddress, RpcSerialize rpcSerialize) {
        this.inetSocketAddress = inetSocketAddress;
        this.rpcSerialize = rpcSerialize;
    }

    public void init() {
        bootstrap = new Bootstrap();
        RpcThreadPoolFactory rpcThreadPoolFactory = RpcThreadPoolFactory.getFactory();
        final CommonClientHandler commonClientHandler = new CommonClientHandler();
        commonClientHandler.setSettableFutureMap(settableFutureMap);
        bootstrap.group(rpcThreadPoolFactory.getBossEventLoopGroup()).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        NettyMessageDecoder nettyMessageDecoder = new NettyMessageDecoder();
                        nettyMessageDecoder.setRpcSerialize(rpcSerialize);
                        NettyMessageEncoder nettyMessageEncoder = new NettyMessageEncoder();
                        nettyMessageEncoder.setRpcSerialize(rpcSerialize);
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
                                .addLast(new LengthFieldPrepender(2))
                                .addLast("idleHandler", new IdleStateHandler(60, 30 , 0))
                                .addLast("connectManager", new ConnectManagerHandler())
                                .addLast("bytesDecoder", new ByteArrayDecoder())
                                .addLast("bytesEncoder", new ByteArrayEncoder())
                                .addLast("nettyDecoder", nettyMessageDecoder)
                                .addLast("nettyEncoder", nettyMessageEncoder)
                                .addLast("logHandler", new LoggingHandler(LogLevel.INFO))
                                .addLast("pingHandler", new HeartBeatClientHandler())
                                .addLast(commonClientHandler);
                    }
                });
        try {
            connect();
        } catch (InterruptedException e) {
            logger.error("connect to server exception", e);
        }
        start.set(true);
    }

    public ChannelFuture connect() throws InterruptedException {
        channelFuture = bootstrap.connect(inetSocketAddress).sync();
        return channelFuture;
    }

    public void destroy() {
        start.set(false);
        if(channelFuture != null) {
            try {
                channelFuture.channel().close().sync();
            } catch (InterruptedException e) {
                logger.error("netty client close exception", e);
            }
        }
    }

    public SettableFuture<Response> invoke(Method method, List<Object> params) {
        if(!start.get()) {
            init();
        }
        Request request = new Request();
        Class<?> declaringClass = method.getDeclaringClass();
        request.setClassName(declaringClass.getName());
        List classes = Lists.newArrayList(method.getParameterTypes());
        request.setClasses(classes);
        request.setMethodName(method.getName());
        request.setParams(params);
        Message message = MessageFactory.buildRequestMessage(request);
        channelFuture.channel().writeAndFlush(message);
        SettableFuture<Response> settableFuture = SettableFuture.create();
        settableFutureMap.put(message.getId(), settableFuture);
        return settableFuture;
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    public void setInetSocketAddress(InetSocketAddress inetSocketAddress) {
        this.inetSocketAddress = inetSocketAddress;
    }

    /**
     * 连接管理
     */
    public class ConnectManagerHandler extends ChannelDuplexHandler {

        public ConnectManagerHandler() {
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            if(!start.get()) {
                return;
            }
            doConnect();
        }
    }

    private void doConnect() {
        RpcThreadPoolFactory rpcThreadPoolFactory = RpcThreadPoolFactory.getFactory();
        EventLoopGroup workerEventLoopGroup = rpcThreadPoolFactory.getWorkerEventLoopGroup();
        workerEventLoopGroup.schedule(new Runnable() {
            @Override
            public void run() {
                bootstrap.connect(inetSocketAddress).addListener(new ChannelFutureListener() {

                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if(future.isSuccess()) {
                            logger.info("the client has connect to the server");
                        } else {
                            doConnect();
                        }
                    }
                });
            }
        }, 2, TimeUnit.SECONDS);
    }
}
