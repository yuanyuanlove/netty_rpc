package com.rpc.netty.common.spring;

import com.google.common.base.Strings;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.AbstractBeanFactory;

/**
 * Created by yuanyuan.pan on 2016/6/13.
 * this is 2016
 */
public class RegistryBean implements BeanFactoryAware {
    private String group;
    private String address;
    private String name;

    private AbstractBeanFactory beanFactory;
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof AbstractBeanFactory) {
            this.beanFactory = (AbstractBeanFactory) beanFactory;
        }
    }

    private String resolve(String value) {
        if (Strings.isNullOrEmpty(value)) return value;
        if (beanFactory == null) return value;
        return beanFactory.resolveEmbeddedValue(value);
    }

    public void resolve() {
        group = resolve(group);
        address = resolve(address);
        name = resolve(name);
    }
}
