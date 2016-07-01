package com.rpc.netty.common.spring;

import com.rpc.netty.common.config.RegistryConfig;
import com.rpc.netty.common.config.ServiceConfig;
import com.rpc.netty.common.serialize.RpcSerialize;
import com.rpc.netty.stub.server.ServiceProvider;
import com.rpc.netty.stub.server.ServiceProviderFactory;

import java.net.InetAddress;

/**
 * Created by yuanyuan.pan on 2016/6/13.
 * this is 2016
 */
public class ProviderBean<T> extends AbstractServiceBean{
    private String id;
    private String version;
    private String group;
    private String ref;
    private String registry;
    private String timeOut;
    private String referenceInterface;

    private ServiceProvider serviceProvider;
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

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public String getReferenceInterface() {
        return referenceInterface;
    }

    public void setReferenceInterface(String referenceInterface) {
        this.referenceInterface = referenceInterface;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    public void destroy() throws Exception {
        serviceProvider.destroy();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        resolveProperty();
        RegistryBean bean = applicationContext.getBean(registry, RegistryBean.class);
        Class<T> aClass = (Class<T>) Class.forName(referenceInterface);
        T serviceBean = applicationContext.getBean(ref, aClass);
        bean.resolve();
        RegistryConfig registryConfig = new RegistryConfig(bean.getGroup(), bean.getAddress());
        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setServiceName(referenceInterface);
        serviceConfig.setTimeOut(Integer.parseInt(timeOut));
        serviceConfig.setVersion(version);
        serviceConfig.setHost(InetAddress.getLocalHost().getHostAddress());
        RpcSerialize rpcSerialize = parseRpcSerialize();
        serviceConfig.setRpcSerialize(rpcSerialize);
        serviceProvider = ServiceProviderFactory.createServiceProvider(serviceConfig, registryConfig, serviceBean);
    }

    private void resolveProperty() {
        id = resolve(id);
        version = resolve(version);
        group = resolve(group);
        registry = resolve(registry);
        timeOut = resolve(timeOut);
        referenceInterface = resolve(referenceInterface);
        ref = resolve(ref);
    }
}
