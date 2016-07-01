package com.rpc.netty.common.zookeeper;

import com.rpc.netty.common.config.ReferenceConfig;
import com.rpc.netty.common.config.RegistryConfig;
import com.rpc.netty.common.config.ServiceConfig;
import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created by yuanyuan.pan on 2016/5/31.
 * this is 2016
 */
public class ZkUtil {
    private static HashMap<RegistryConfig,CuratorFramework> curatorFrameworkHashMap = Maps.newHashMap();

    public static String generateServicePath(ServiceConfig serviceConfig) throws UnknownHostException {
        return "/" + serviceConfig.getServiceName() + "/provider/" +  InetAddress.getLocalHost().getHostName() + ":" + serviceConfig.getPort();
    }

    public static String generateConsumerPath(ReferenceConfig referenceConfig) throws UnknownHostException {
        return "/" + referenceConfig.getServiceName() + "/consumer/" +  InetAddress.getLocalHost().getHostName() + ":" + referenceConfig.getPort();
    }

    public static String generateReferenceWatchPath(ReferenceConfig referenceConfig) {
        return "/" + referenceConfig.getServiceName() + "/provider";
    }

    public static CuratorFramework getOrCreateZkClient(RegistryConfig registryConfig) {
        if(curatorFrameworkHashMap.containsKey(curatorFrameworkHashMap)) {
            return curatorFrameworkHashMap.get(curatorFrameworkHashMap);
        }
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectionTimeoutMs(3000).connectString(registryConfig.getZkAddress())
                .namespace(registryConfig.getGroup()).sessionTimeoutMs(50000).retryPolicy(new RetryUntilElapsed(50000, 5000)).build();
        curatorFramework.start();
        curatorFramework.getConnectionStateListenable();
        curatorFrameworkHashMap.put(registryConfig, curatorFramework);
        return curatorFramework;
    }


    public static void removeZkClient(RegistryConfig registryConfig) {
        CuratorFramework remove = curatorFrameworkHashMap.remove(registryConfig);
        remove.close();
    }


}
