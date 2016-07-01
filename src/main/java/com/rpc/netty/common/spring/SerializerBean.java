package com.rpc.netty.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by yuanyuan.pan on 2016/6/29.
 * this is 2016
 */
public class SerializerBean {
    private String serializer;
    private String serializerConfig;

    public String getSerializer() {
        return serializer;
    }

    public void setSerializer(String serializer) {
        this.serializer = serializer;
    }

    public String getSerializerConfig() {
        return serializerConfig;
    }

    public void setSerializerConfig(String serializerConfig) {
        this.serializerConfig = serializerConfig;
    }
}
