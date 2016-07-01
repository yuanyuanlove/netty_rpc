package com.rpc.netty.common.zookeeper;

import com.rpc.netty.common.config.ReferenceConfig;
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
public class ConsumerStateListener implements ConnectionStateListener{
    private ReferenceConfig referenceConfig;
    private ServiceDiscovery serviceDiscovery;

    private Logger logger = LoggerFactory.getLogger(ConsumerStateListener.class);

    public ConsumerStateListener(ReferenceConfig referenceConfig, ServiceDiscovery serviceDiscovery) {
        this.referenceConfig = referenceConfig;
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        logger.info("receive new state message{}", newState);
        if (newState == ConnectionState.LOST) {
            while (true) {
                try {
                    if (client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                        serviceDiscovery.registryConsumer(referenceConfig);
                        break;
                    }
                } catch (InterruptedException e) {
                    logger.info("receive InterruptedException", e);
                    break;
                } catch (Exception e) {
                    logger.error("consumer connect exception", e);
                }
            }
        }
    }
}
