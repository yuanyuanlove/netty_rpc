package com.rpc.netty.common.spring;

import com.rpc.netty.common.config.ReferenceConfig;
import com.rpc.netty.common.config.RegistryConfig;
import com.rpc.netty.common.serialize.RpcSerialize;
import com.rpc.netty.common.serialize.SerializeConfig;
import com.rpc.netty.common.serialize.fst.FSTRpcSerialize;
import com.rpc.netty.common.serialize.kryo.KryoRpcSerialize;
import com.rpc.netty.common.serialize.kryo.KryoSerializeConfig;
import com.rpc.netty.stub.client.Client;
import com.rpc.netty.stub.client.impl.ReferenceClientCluster;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by yuanyuan.pan on 2016/6/13.
 * this is 2016
 */
public class ConsumerBean<T> extends AbstractServiceBean implements FactoryBean<T> {
    private String id;
    private String version;
    private String group;
    private String referenceInterface;
    private String registry;
    private String timeOut;

    private Client client;
    private T targetProxy;
    private Class<T> objectClass;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getReferenceInterface() {
        return referenceInterface;
    }

    public void setReferenceInterface(String referenceInterface) {
        this.referenceInterface = referenceInterface;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    public void destroy() throws Exception {
        client.destroy();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        resolveProperty();
        RegistryBean bean = applicationContext.getBean(registry, RegistryBean.class);

        RpcSerialize rpcSerialize = parseRpcSerialize();
        bean.resolve();
        RegistryConfig registryConfig = new RegistryConfig(bean.getGroup(), bean.getAddress());
        ReferenceConfig referenceConfig = new ReferenceConfig();
        referenceConfig.setServiceName(referenceInterface);
        referenceConfig.setRpcSerialize(rpcSerialize);
        referenceConfig.setTimeOut(Integer.parseInt(timeOut));
        client = new ReferenceClientCluster(referenceConfig, registryConfig);
        targetProxy = (T) client.getTargetProxy();
        objectClass = (Class<T>) Class.forName(referenceConfig.getServiceName());
    }

    private void resolveProperty() {
        id = resolve(id);
        version = resolve(version);
        group = resolve(group);
        referenceInterface = resolve(referenceInterface);
        registry = resolve(registry);
        timeOut = resolve(timeOut);
    }

    @Override
    public T getObject() throws Exception {
        return targetProxy;
    }

    @Override
    public Class<?> getObjectType() {
        return objectClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
