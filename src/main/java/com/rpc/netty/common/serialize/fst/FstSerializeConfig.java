package com.rpc.netty.common.serialize.fst;

import com.google.common.collect.Lists;
import com.rpc.netty.common.serialize.AbstractSerializeConfig;
import com.rpc.netty.common.serialize.SerializeConfig;

import java.util.List;

/**
 * Created by yuanyuan.pan on 2016/6/21.
 * this is 2016
 */
public class FstSerializeConfig extends AbstractSerializeConfig {

    @Override
    protected List<Class> getUserDefineClass() {
        return Lists.newArrayList();
    }
}
