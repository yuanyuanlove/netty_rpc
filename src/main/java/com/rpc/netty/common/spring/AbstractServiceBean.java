package com.rpc.netty.common.spring;

import com.google.common.base.Strings;
import com.rpc.netty.common.serialize.RpcSerialize;
import com.rpc.netty.common.serialize.SerializeConfig;
import com.rpc.netty.common.serialize.fst.FSTRpcSerialize;
import com.rpc.netty.common.serialize.fst.FstSerializeConfig;
import com.rpc.netty.common.serialize.kryo.KryoRpcSerialize;
import com.rpc.netty.common.serialize.kryo.KryoSerializeConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by yuanyuan.pan on 2016/6/14.
 * this is 2016
 */
public abstract class AbstractServiceBean implements BeanFactoryAware, ApplicationContextAware, InitializingBean, DisposableBean {
    protected ApplicationContext applicationContext;
    private AbstractBeanFactory beanFactory;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (AbstractBeanFactory) beanFactory;
    }

    protected String resolve(String value) {
        if (Strings.isNullOrEmpty(value)) return value;
        if (beanFactory == null) return value;
        return beanFactory.resolveEmbeddedValue(value);
    }

    protected RpcSerialize parseRpcSerialize() {
        try {
            SerializerBean serializerBean =  applicationContext.getBean(SerializerBean.class);
            RpcSerialize rpcSerialize = null;
            if(serializerBean == null) {
                rpcSerialize = new FSTRpcSerialize(new FstSerializeConfig());
            } else {
                String serializer = serializerBean.getSerializer();
                if(serializer.equals("kryo")) {

                    Class<?> aClass = Class.forName(serializerBean.getSerializerConfig());
                    SerializeConfig o = (SerializeConfig) aClass.newInstance();
                    rpcSerialize = new KryoRpcSerialize(o);
                    rpcSerialize.init(o);
                } else if(serializer.equals("fst")) {
                    Class<?> aClass = Class.forName(serializerBean.getSerializerConfig());
                    SerializeConfig o = (SerializeConfig) aClass.newInstance();
                    rpcSerialize = new FSTRpcSerialize(o);
                    rpcSerialize.init(o);
                } else {
                    throw new RuntimeException("cannot find match rpc serializer");
                }
            }
            return rpcSerialize;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
