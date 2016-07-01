package com.rpc.netty.common.serialize;

/**
 * Created by yuanyuan.pan on 2016/6/17.
 * this is 2016
 */
public interface RpcSerialize {
    void init(SerializeConfig serializeConfig);
    byte[] encode(Object object);
    Object decode(byte[] bytes);
}
