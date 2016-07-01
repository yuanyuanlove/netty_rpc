package com.rpc.netty;

import com.rpc.netty.api.ITestService;
import com.rpc.netty.common.config.RegistryConfig;
import com.rpc.netty.common.config.ServiceConfig;
import com.rpc.netty.common.serialize.fst.FSTRpcSerialize;
import com.rpc.netty.common.serialize.fst.FstSerializeConfig;
import com.rpc.netty.common.serialize.kryo.KryoRpcSerialize;
import com.rpc.netty.stub.server.ServiceProvider;
import com.rpc.netty.stub.server.ServiceProviderFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yuanyuan.pan on 2016/6/22.
 * this is 2016
 */

public class ServiceProviderTest {
    private static ServiceProvider serviceProvider;
    private static Logger logger = LoggerFactory.getLogger(ServiceProviderTest.class);
    @BeforeClass
    public static void init() {
        try {
            ServiceConfig serviceConfig = new ServiceConfig();
            serviceConfig.setRpcSerialize(new FSTRpcSerialize(new FstSerializeConfig()));
            serviceConfig.setVersion("1.0.0");
            serviceConfig.setHost("127.0.0.1");
            serviceConfig.setPort(9090);
            serviceConfig.setTimeOut(3000);
            serviceConfig.setServiceName(ITestService.class.getName());
            RegistryConfig registryConfig = new RegistryConfig("test", "");
            ITestService iTestService = new ITestService() {
                @Override
                public String hello(String name) {
                    return "yuanyuan";
                }
            };
            serviceProvider = ServiceProviderFactory.createServiceProvider(serviceConfig, registryConfig, iTestService);
        } catch (Exception e) {
            logger.error("server create error", e);
        }

    }


    @Test
    public void testBind() throws Exception {
        try {
            serviceProvider.bind();
        } catch (Exception e) {
            logger.error("server init error", e);
        }

    }

    @Test
    public void testDestroy() throws Exception {
        serviceProvider.bind();
        serviceProvider.destroy();
    }

}