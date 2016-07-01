package com.rpc.netty.stub.server;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Created by yuanyuan.pan on 2016/6/22.
 * this is 2016
 */
public interface Server {
    void destroy();
    void bind();
}
