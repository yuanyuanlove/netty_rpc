package com.rpc.netty.protocol.model;

import java.io.Serializable;

/**
 * 响应
 * Created by yuanyuan on 2016/5/29.
 */
public class  Response implements Serializable{
    private Object response;

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
