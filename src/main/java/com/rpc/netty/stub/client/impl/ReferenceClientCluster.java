package com.rpc.netty.stub.client.impl;

import com.rpc.netty.common.config.ReferenceConfig;
import com.rpc.netty.common.config.RegistryConfig;
import com.rpc.netty.common.config.ServiceConfig;
import com.rpc.netty.common.loadbance.LoadBalance;
import com.rpc.netty.common.loadbance.RandomLoadBalance;
import com.rpc.netty.protocol.model.Response;
import com.rpc.netty.common.zookeeper.ServiceDiscovery;
import com.rpc.netty.common.zookeeper.ZkUtil;
import com.rpc.netty.common.zookeeper.impl.ZkServiceDiscoveryImpl;
import com.rpc.netty.common.util.JsonUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.SettableFuture;
import com.rpc.netty.stub.client.proxy.CglibReferenceProxy;
import com.rpc.netty.stub.client.Client;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 支持负载均衡和服务发现的客户端
 * Created by yuanyuan.pan on 2016/5/31.
 * this is 2016
 */
public class ReferenceClientCluster<T> implements TreeCacheListener, Client {
    private CuratorFramework client;
    private ServiceDiscovery serviceDiscovery;
    private LoadBalance loadBalance = new RandomLoadBalance();
    private volatile ReferenceConfig referenceConfig;
    private volatile Map<String, SimpleReferenceClient> referenceClientMap;
    private Object targetProxy;
    private Logger logger = LoggerFactory.getLogger(ReferenceClientCluster.class);

    public ReferenceClientCluster(ReferenceConfig referenceConfig, RegistryConfig registryConfig) {
        this.client = ZkUtil.getOrCreateZkClient(registryConfig);
        this.referenceConfig = referenceConfig;
        this.init();
    }

    public ReferenceClientCluster(ReferenceConfig referenceConfig, CuratorFramework client) {
        this.client = client;
        this.referenceConfig = referenceConfig;
        this.init();
    }

    /**
     * 定时刷新,初始化
     */
    public void init() {
        try {
            referenceClientMap = Maps.newConcurrentMap();
            serviceDiscovery = new ZkServiceDiscoveryImpl(client);
            serviceDiscovery.registryConsumer(referenceConfig);
            generateServiceMap();
        } catch (Exception e) {
            logger.error("reference config init error", e);
        }
        CglibReferenceProxy cglibReferenceProxy = new CglibReferenceProxy(this, referenceConfig);
        Class targetInterface = null;
        try {
            targetInterface = Class.forName(referenceConfig.getServiceName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("cannot find expect class");
        }
        targetProxy = (T) cglibReferenceProxy.createProxy(targetInterface);
    }

    private void generateServiceMap() throws ClassNotFoundException {
        Map<String, ServiceConfig> serviceConfigMap = serviceDiscovery.getServiceConfig();
        Set<Map.Entry<String, ServiceConfig>> entries = serviceConfigMap.entrySet();
        for(Map.Entry<String, ServiceConfig> entry : entries) {
            String key = entry.getKey();
            ServiceConfig value = entry.getValue();
            addServiceReference(key, value);
        }
    }

    /**
     * 子节点改变的事件
     * @param client
     * @param event
     * @throws Exception
     */
    @Override
    public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
        logger.debug("receive event type is{}, and refresh the config", event.getType());
        switch (event.getType()) {
            case NODE_ADDED:
                ChildData data = event.getData();
                processAdded(data);
                break;
            case NODE_REMOVED:
                data = event.getData();
                processRemove(data);
                break;
            case NODE_UPDATED:
                data = event.getData();
                processAdded(data);
                break;
        }
    }

    private void processAdded(ChildData data) {
        String path = data.getPath();
        byte[] rawData = data.getData();
        try {
            ServiceConfig serviceConfig = JsonUtil.parseObject(new String(rawData), ServiceConfig.class);
            addServiceReference(path, serviceConfig);
        } catch (Exception e) {
            logger.error("data parse exception");
        }
    }

    private void addServiceReference(String path, ServiceConfig serviceConfig) throws ClassNotFoundException {
        Class t = Class.forName(serviceConfig.getServiceName());
        SimpleReferenceClient simpleReferenceClient = new SimpleReferenceClient(serviceConfig, referenceConfig, t);
        referenceClientMap.put(path, simpleReferenceClient);
    }

    private void processRemove(ChildData data) {
        String path = data.getPath();
        SimpleReferenceClient simpleReferenceClient = referenceClientMap.get(path);
        simpleReferenceClient.destroy();
    }

    public void destroy() {

    }

    @Override
    public Object getTargetProxy() {
        return targetProxy;
    }

    @Override
    public SettableFuture<Response> invoke(Method method, List params) {
        if(referenceClientMap.size() == 0) {
            try {
                generateServiceMap();
            } catch (ClassNotFoundException e) {
                logger.error("cannot find class", e);
            }
        }
        List<SimpleReferenceClient> simpleReferenceClients = Lists.newArrayList(referenceClientMap.values());
        SimpleReferenceClient simpleReferenceClient = loadBalance.selectOne(simpleReferenceClients, params);
        return simpleReferenceClient.invoke(method, params);
    }

}
