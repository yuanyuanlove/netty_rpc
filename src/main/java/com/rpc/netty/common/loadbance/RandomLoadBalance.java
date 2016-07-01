package com.rpc.netty.common.loadbance;

import com.rpc.netty.stub.client.impl.SimpleReferenceClient;

import java.util.List;

/**
 * Created by yuanyuan.pan on 2016/6/1.
 * this is 2016
 */
public class RandomLoadBalance implements LoadBalance {

    @Override
    public SimpleReferenceClient selectOne(List<SimpleReferenceClient> clients, List<Object> params) {
        if(clients.size() == 0) {
            return null;
        }
        if(clients.size() == 1) {
            return clients.get(0);
        }
        int hashCode = System.identityHashCode(params);
        int index = hashCode % clients.size();
        return clients.get(index);
    }
}
