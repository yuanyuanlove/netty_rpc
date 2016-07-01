package com.rpc.netty.common.spring.parser;

import com.rpc.netty.common.spring.RegistryBean;
import com.rpc.netty.common.spring.SerializerBean;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * Created by yuanyuan.pan on 2016/6/29.
 * this is 2016
 */
public class SerializeParser extends AbstractSimpleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return SerializerBean.class;
    }
}
