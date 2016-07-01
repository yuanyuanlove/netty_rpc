package com.rpc.netty.protocol.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * message工厂
 * Created by yuanyuan.pan on 2016/5/30.
 * this is 2016
 */
public class MessageFactory {
    private static AtomicLong atomicLong = new AtomicLong(0l);

    public static Message buildRequestMessage(Request request) {
        Message message = new Message();
        message.setMessageType(MessageType.request);
        message.setId(String.valueOf(atomicLong.incrementAndGet()));
        message.setRequest(request);
        return message;
    }


    public static Message buildResponseMessage(Response response, String messageId) {
        Message message = new Message();
        message.setMessageType(MessageType.response);
        message.setId(messageId);
        message.setResponse(response);
        return message;
    }


    public static Message buildPingMessage() {
        Message message = new Message();
        message.setMessageType(MessageType.ping);
        return message;
    }

    public static Message buildPongMessage() {
        Message message = new Message();
        message.setMessageType(MessageType.pang);
        return message;
    }
}
