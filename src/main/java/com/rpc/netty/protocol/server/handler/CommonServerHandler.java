package com.rpc.netty.protocol.server.handler;

import com.rpc.netty.protocol.model.Message;
import com.rpc.netty.protocol.model.MessageFactory;
import com.rpc.netty.protocol.model.Request;
import com.rpc.netty.protocol.model.Response;
import com.rpc.netty.protocol.threadpool.RpcThreadPoolFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by yuanyuan on 2016/5/29.
 */
public class CommonServerHandler extends SimpleChannelInboundHandler<Message> {

    private Object objectTarget;
    private Class targetInterface;
    private ExecutorService executorService = RpcThreadPoolFactory.getFactory().getBusinessExecutor();
    public Class getTargetInterface() {
        return targetInterface;
    }

    public void setTargetInterface(Class targetInterface) {
        this.targetInterface = targetInterface;
    }

    public Object getObjectTarget() {
        return objectTarget;
    }

    public void setObjectTarget(Object objectTarget) {
        this.objectTarget = objectTarget;
    }

    private Logger logger = LoggerFactory.getLogger(CommonServerHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        Request request = message.getRequest();
        Method declaredMethod = targetInterface.getDeclaredMethod(request.getMethodName(), request.getClasses().toArray(new Class[0]));
        Future future = asyncRun(request, declaredMethod);
        Response response = new Response();
        response.setResponse(future.get());
        Message responseMessage = MessageFactory.buildResponseMessage(response, message.getId());
        ctx.writeAndFlush(responseMessage);
    }

    private Future asyncRun(final Request request, final Method declaredMethod) throws IllegalAccessException, InvocationTargetException {
        return executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return declaredMethod.invoke(objectTarget, request.getParams().toArray(new Object[0]));
            }
        });
    }
}
