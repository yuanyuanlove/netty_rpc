package com.rpc.netty.common.spring.parser;

import com.rpc.netty.common.spring.ProviderBean;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * Created by yuanyuan.pan on 2016/6/13.
 * this is 2016
 */
public class ProviderParser extends AbstractSimpleBeanDefinitionParser{
    @Override
    protected Class<?> getBeanClass(Element element) {
        return ProviderBean.class;
    }
}
