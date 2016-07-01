package com.rpc.netty.protocol.model;

import java.io.Serializable;

/**
 * message
 * Created by yuanyuan.pan on 2016/5/30.
 * this is 2016
 */
public class Message implements Serializable{

    private static final long serialVersionUID = 8935156699794373330L;
    private MessageType messageType;
    private Request request;
    private Response response;
    private String id;
    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
