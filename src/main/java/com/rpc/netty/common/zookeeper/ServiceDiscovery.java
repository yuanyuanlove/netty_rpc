package com.rpc.netty.common.zookeeper;

import com.rpc.netty.common.config.ReferenceConfig;
import com.rpc.netty.common.config.ServiceConfig;

import java.util.Map;
/**
 * 服务发现
 * Created by yuanyuan.pan on 2016/5/31.
 * this is 2016
 */
public interface ServiceDiscovery {
    void registryProvider(ServiceConfig serviceConfig);
    void registryConsumer(ReferenceConfig referenceConfig);
    Map<String, ServiceConfig> getServiceConfig();
}
