package com.rpc.netty.common.serialize.kryo;

import com.google.common.collect.Lists;
import com.rpc.netty.common.serialize.AbstractSerializeConfig;

import java.util.List;

/**
 * Created by yuanyuan.pan on 2016/6/21.
 * this is 2016
 */
public class KryoSerializeConfig extends AbstractSerializeConfig {
    @Override
    protected List<Class> getUserDefineClass() {
        return Lists.newArrayList();
    }
}
