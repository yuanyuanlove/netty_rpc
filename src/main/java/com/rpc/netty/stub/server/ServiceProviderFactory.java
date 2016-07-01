package com.rpc.netty.stub.server;

import com.google.common.util.concurrent.MoreExecutors;
import com.rpc.netty.common.config.RegistryConfig;
import com.rpc.netty.common.config.ServiceConfig;
import com.rpc.netty.common.zookeeper.ZkUtil;
import com.google.common.collect.Maps;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.curator.framework.CuratorFramework;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by yuanyuan.pan on 2016/6/1.
 * this is 2016
 */
public class ServiceProviderFactory {
    private static Map<RegistryConfig, CuratorFramework> curatorFrameworkMap = Maps.newConcurrentMap();
    public static ServiceProvider createServiceProvider(ServiceConfig serviceConfig, RegistryConfig registryConfig, Object targetObject) {
        CuratorFramework client;
        if(curatorFrameworkMap.containsKey(registryConfig)) {
            client = curatorFrameworkMap.get(registryConfig);
        } else {
            client = ZkUtil.getOrCreateZkClient(registryConfig);
            curatorFrameworkMap.put(registryConfig, client);
        }
        ServiceProvider serviceProvider = new ServiceProvider(serviceConfig, client, targetObject);
        serviceProvider.bind();
        return serviceProvider;
    }
}
