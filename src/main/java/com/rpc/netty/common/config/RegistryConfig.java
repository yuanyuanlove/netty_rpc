package com.rpc.netty.common.config;

/**
 * 注册中心
 * Created by yuanyuan.pan on 2016/5/30.
 * this is 2016
 */
public class RegistryConfig {
    private String group;
    private String zkAddress;

    public RegistryConfig(String group, String zkAddress) {
        this.group = group;
        this.zkAddress = zkAddress;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistryConfig)) return false;

        RegistryConfig that = (RegistryConfig) o;

        if (getGroup() != null ? !getGroup().equals(that.getGroup()) : that.getGroup() != null) return false;
        return getZkAddress() != null ? getZkAddress().equals(that.getZkAddress()) : that.getZkAddress() == null;

    }

    @Override
    public int hashCode() {
        int result = getGroup() != null ? getGroup().hashCode() : 0;
        result = 31 * result + (getZkAddress() != null ? getZkAddress().hashCode() : 0);
        return result;
    }
}
