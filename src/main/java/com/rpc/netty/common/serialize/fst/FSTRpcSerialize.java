package com.rpc.netty.common.serialize.fst;

import com.rpc.netty.common.serialize.RpcSerialize;
import com.rpc.netty.common.serialize.SerializeConfig;
import org.nustaq.serialization.FSTConfiguration;

import java.util.List;

/**
 * Created by yuanyuan.pan on 2016/6/21.
 * this is 2016
 */
public class FSTRpcSerialize implements RpcSerialize {
    private List<Class> registryClasses;
    private boolean share;

    public FSTRpcSerialize() {
    }

    private ThreadLocal<FSTConfiguration> conf = new ThreadLocal<FSTConfiguration>() {
        public FSTConfiguration initialValue() {
            FSTConfiguration defaultConfiguration = FSTConfiguration.createDefaultConfiguration();
            for(Class aClass : registryClasses) {
                defaultConfiguration.registerClass(aClass);
            }
            defaultConfiguration.setShareReferences(share);
            return defaultConfiguration;
        }
    };

    public FSTRpcSerialize(SerializeConfig serializeConfig) {
        init(serializeConfig);
    }

    @Override
    public void init(final SerializeConfig serializeConfig) {
        registryClasses = serializeConfig.getRegistryClasses();
        share = serializeConfig.share();
    }

    @Override
    public byte[] encode(Object object) {
        FSTConfiguration fstConfiguration = conf.get();
        return fstConfiguration.asByteArray(object);
    }

    @Override
    public Object decode(byte[] bytes) {
        FSTConfiguration fstConfiguration = conf.get();
        return fstConfiguration.asObject(bytes);
    }
}
