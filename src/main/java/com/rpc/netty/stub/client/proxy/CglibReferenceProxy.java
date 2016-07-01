package com.rpc.netty.stub.client.proxy;

import com.rpc.netty.common.config.ReferenceConfig;
import com.rpc.netty.protocol.model.Response;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.SettableFuture;
import com.rpc.netty.stub.client.Client;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 客户端的动态代理
 * Created by yuanyuan on 2016/5/29.
 */
public class CglibReferenceProxy implements MethodInterceptor {
    private Logger logger = LoggerFactory.getLogger(CglibReferenceProxy.class);
    private Client referenceClient;
    private ReferenceConfig referenceConfig;

    public CglibReferenceProxy(Client referenceClient, ReferenceConfig referenceConfig) {
        this.referenceClient = referenceClient;
        this.referenceConfig = referenceConfig;
    }

    public Object createProxy(Class target) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target);// 设置代理目标
        enhancer.setCallback(this);// 设置回调
        enhancer.setClassLoader(target.getClass().getClassLoader());
        return enhancer.create();
    }



    /**
     * 在代理实例上处理方法调用并返回结果
     *
     * @param proxy
     *            代理类
     * @param method
     *            被代理的方法
     * @param params
     *            该方法的参数数组
     * @param methodProxy
     */
    public Object intercept(Object proxy, Method method, Object[] params,
                            MethodProxy methodProxy) throws Throwable {
        // 调用原始对象的方法
        SettableFuture<Response> invoke = referenceClient.invoke(method, Lists.newArrayList(params));
        Response response = invoke.get(referenceConfig.getTimeOut(), TimeUnit.MILLISECONDS);
        return response.getResponse();
    }
}