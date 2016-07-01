package com.rpc.netty.common.loadbance;

import com.rpc.netty.stub.client.impl.SimpleReferenceClient;

import java.util.List;

/**
 * Created by yuanyuan.pan on 2016/6/1.
 * this is 2016
 */
public interface LoadBalance {
    SimpleReferenceClient selectOne(List<SimpleReferenceClient> clients, List<Object> params);
}
