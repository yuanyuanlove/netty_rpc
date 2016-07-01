package com.rpc.netty.common.spring;

import com.rpc.netty.common.spring.parser.ConsumerParser;
import com.rpc.netty.common.spring.parser.ProviderParser;
import com.rpc.netty.common.spring.parser.RegistryParser;
import com.rpc.netty.common.spring.parser.SerializeParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by yuanyuan.pan on 2016/5/31.
 * this is 2016
 */
public class RpcNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("consumer", new ConsumerParser());
        registerBeanDefinitionParser("provider", new ProviderParser());
        registerBeanDefinitionParser("registry", new RegistryParser());
        registerBeanDefinitionParser("serializer", new SerializeParser());
    }
}
