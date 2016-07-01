package com.rpc.netty.stub.client;

import com.rpc.netty.protocol.model.Response;
import com.google.common.util.concurrent.SettableFuture;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by yuanyuan.pan on 2016/6/1.
 * this is 2016
 */
public interface Client<T> {
    void destroy();
    T getTargetProxy();
    SettableFuture<Response> invoke(Method method, List<Object> params);
}
