package com.rpc.netty.common.zookeeper;

import com.rpc.netty.common.config.ServiceConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 进行无限重试
 * Created by yuanyuan.pan on 2016/6/2.
 * this is 2016
 */
public class ProviderConnectionStateListener implements ConnectionStateListener {
    private ServiceConfig serviceConfig;
    private ServiceDiscovery serviceDiscovery;


    private Logger logger = LoggerFactory.getLogger(ProviderConnectionStateListener.class);

    public ProviderConnectionStateListener(ServiceConfig serviceConfig, ServiceDiscovery serviceDiscovery) {
        this.serviceConfig = serviceConfig;
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        logger.info("receive state change message:{}", newState);
        if (newState == ConnectionState.LOST) {
            logger.info("receive lost message");
            while (true) {
                try {
                    if (client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                        serviceDiscovery.registryProvider(serviceConfig);
                        break;
                    }
                } catch (InterruptedException e) {
                    logger.info("receive InterruptedException", e);
                    break;
                } catch (Exception e) {
                    logger.error("provider connect exception", e);
                }
            }
        }
    }
}
