package com.rpc.netty.protocol.model;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 请求
 * Created by yuanyuan on 2016/5/29.
 */
public class Request implements Serializable{
    private String methodName;
    private String className;
    private List<Object> params;
    private List<Class> classes;

    private static AtomicLong atomicLong = new AtomicLong();

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }


}



