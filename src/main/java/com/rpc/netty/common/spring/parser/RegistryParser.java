package com.rpc.netty.common.spring.parser;

import com.rpc.netty.common.spring.RegistryBean;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * Created by yuanyuan.pan on 2016/6/14.
 * this is 2016
 */
public class RegistryParser  extends AbstractSimpleBeanDefinitionParser {
    @Override
    protected Class<?> getBeanClass(Element element) {
        return RegistryBean.class;
    }
}
