package com.rpc.netty.common.serialize;

import java.util.List;

/**
 * Created by yuanyuan.pan on 2016/6/21.
 * this is 2016
 */
public interface SerializeConfig {
    /**
     * 是否需要注册
     * @return
     */
    List<Class> getRegistryClasses();

    /**
     * 是否需要探测循环引用,默认为true，提高性能
     */
    boolean share();
}
