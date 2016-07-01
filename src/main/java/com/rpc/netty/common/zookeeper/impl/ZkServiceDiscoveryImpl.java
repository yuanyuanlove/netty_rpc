package com.rpc.netty.common.zookeeper.impl;

import com.rpc.netty.common.config.ReferenceConfig;
import com.rpc.netty.common.config.ServiceConfig;
import com.rpc.netty.common.zookeeper.ConsumerStateListener;
import com.rpc.netty.common.zookeeper.ProviderConnectionStateListener;
import com.rpc.netty.common.zookeeper.ServiceDiscovery;
import com.rpc.netty.common.zookeeper.ZkUtil;
import com.rpc.netty.common.util.JsonUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 * Created by yuanyuan.pan on 2016/5/31.
 * this is 2016
 */
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {
    private CuratorFramework curatorFramework;
    private TreeCache treeCache;
    private String watcherPath;
    private ReferenceConfig referenceConfig;
    private Logger logger = LoggerFactory.getLogger(ZkServiceDiscoveryImpl.class);

    @Override
    public void registryProvider(ServiceConfig serviceConfig) {
        try {
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(ZkUtil.generateServicePath(serviceConfig), JsonUtil.toJson(serviceConfig).getBytes(Charsets.UTF_8));
            curatorFramework.getConnectionStateListenable().addListener(new ProviderConnectionStateListener(serviceConfig, this), Executors.newSingleThreadExecutor());
        } catch (KeeperException.NodeExistsException nodeExistsException) {
            logger.info("the node is exist");
        } catch (Exception e) {
            logger.error("service registry exception", e);
            throw  new RuntimeException("service registry exception");
        }
    }

    @Override
    public void registryConsumer(ReferenceConfig referenceConfig) {
        this.referenceConfig = referenceConfig;
        try {
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(ZkUtil.generateConsumerPath(referenceConfig), JsonUtil.toJson(referenceConfig).getBytes(Charsets.UTF_8));
        } catch (KeeperException.NodeExistsException nodeExistsException) {
            logger.info("the node is exist");
        } catch (Exception e) {
            logger.error("consumer registry exception", e);
        }
        initServiceWatcher(referenceConfig);
    }

    private void initServiceWatcher(ReferenceConfig referenceConfig) {
        treeCache = watchProvider(referenceConfig);
        curatorFramework.getConnectionStateListenable().addListener(new ConsumerStateListener(referenceConfig, this));
    }

    @Override
    public Map<String, ServiceConfig> getServiceConfig() {
        Map<String, ServiceConfig> serviceConfigs = Maps.newConcurrentMap();
        if(treeCache == null) {
            initServiceWatcher(referenceConfig);
        }
        Map<String, ChildData> currentChildren = treeCache.getCurrentChildren(watcherPath);
        if(currentChildren == null) {
            return Maps.newConcurrentMap();
        }
        Set<Map.Entry<String, ChildData>> entries = currentChildren.entrySet();
        for(Map.Entry<String, ChildData> entry : entries) {
            try {
                ChildData value = entry.getValue();
                String key = entry.getKey();
                ServiceConfig serviceConfig = JsonUtil.parseObject(new String(value.getData()), ServiceConfig.class);
                serviceConfigs.put(key, serviceConfig);
            } catch (IOException e) {
                logger.error("json parse exception", e);
            }
        }
        return serviceConfigs;
    }


    public TreeCache watchProvider(ReferenceConfig referenceConfig) {
        try {
            watcherPath = ZkUtil.generateReferenceWatchPath(referenceConfig);
            TreeCache treeCache = TreeCache.newBuilder(curatorFramework, watcherPath).setCacheData(true).build();
            treeCache.start();
            return treeCache;
        } catch (Exception e) {
            logger.error("consumer registry exception", e);
        }
        return null;
    }

    public ZkServiceDiscoveryImpl(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

}
