package com.rpc.netty.common.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Kryo.DefaultInstantiatorStrategy;
import com.esotericsoftware.kryo.io.*;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.esotericsoftware.kryo.serializers.VersionFieldSerializer;
import com.google.common.collect.Lists;
import com.rpc.netty.common.serialize.RpcSerialize;
import com.rpc.netty.common.serialize.SerializeConfig;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.util.List;

/**
 * kryo序列化方式，提供前后兼容
 * Created by yuanyuan.pan on 2016/6/17.
 * this is 2016
 */
public class KryoRpcSerialize implements RpcSerialize {
    private final int UNSAFE_OUTPUT_BUFFER_SIZE = 20480;
    private final int UNSAFE_OUTPUT_BUFFER_MAX_SIZE = 204800;
    private volatile List<Class> registerClass = Lists.newArrayList();
    private boolean share;

    private KryoPool pool;

    public KryoRpcSerialize() {
    }

    public KryoRpcSerialize(SerializeConfig serializeConfig) {
        init(serializeConfig);
        KryoFactory factory = new KryoFactory() {
            @Override
            public Kryo create() {
                Kryo kryo = new Kryo();
                kryo.setDefaultSerializer(VersionFieldSerializer.class);
                DefaultInstantiatorStrategy defaultInstantiatorStrategy = new DefaultInstantiatorStrategy(new StdInstantiatorStrategy());
                kryo.setInstantiatorStrategy(defaultInstantiatorStrategy);
                for(Class aClass : registerClass) {
                    kryo.register(aClass);
                }
                kryo.setReferences(share);
                return kryo;
            }
        };
        pool = new KryoPool.Builder(factory).build();
    }

    public void init(SerializeConfig serializeConfig) {
        registerClass = serializeConfig.getRegistryClasses();
        share = serializeConfig.share();
    }


    @Override
    public byte[] encode(Object object) {
        Kryo kryo = pool.borrow();
        Output output = new UnsafeOutput(UNSAFE_OUTPUT_BUFFER_SIZE, UNSAFE_OUTPUT_BUFFER_MAX_SIZE);
        try {
            kryo.writeClassAndObject(output, object);
            byte[] buffer = output.getBuffer();
            return buffer;
        } finally {
            output.close();
            pool.release(kryo);
        }

    }

    @Override
    public Object decode(byte[] bytes) {
        Kryo kryo = pool.borrow();
        Input input = new UnsafeInput(bytes);
        try {
            Object o = kryo.readClassAndObject(input);
            return o;
        } finally {
            input.close();
            pool.release(kryo);
        }
    }
}
