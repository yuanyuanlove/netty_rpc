package com.rpc.netty.common.serialize;

import com.google.common.collect.Lists;
import com.rpc.netty.protocol.model.Message;
import com.rpc.netty.protocol.model.MessageType;
import com.rpc.netty.protocol.model.Request;
import com.rpc.netty.protocol.model.Response;

import java.util.List;

/**
 * Created by yuanyuan.pan on 2016/6/21.
 * this is 2016
 */
public abstract class AbstractSerializeConfig implements SerializeConfig {
    @Override
    public List<Class> getRegistryClasses() {
        List<Class> classes = Lists.newArrayList();
        classes.add(Message.class);
        classes.add(MessageType.class);
        classes.add(Request.class);
        classes.add(Response.class);
        classes.addAll(getUserDefineClass());
        return classes;
    }

    protected abstract List<Class> getUserDefineClass();

    @Override
    public boolean share() {
        return false;
    }
}
