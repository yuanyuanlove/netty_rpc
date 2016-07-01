package com.rpc.netty.common.loadbance;

import com.rpc.netty.stub.client.impl.SimpleReferenceClient;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yuanyuan.pan on 2016/6/2.
 * this is 2016
 */
public class WeightRoundRobinLoadBalance implements LoadBalance {
    private volatile AtomicInteger currentWeight;

    public WeightRoundRobinLoadBalance() {
        Random random = new Random();
        this.currentWeight = new AtomicInteger(0);
    }

    @Override
    public SimpleReferenceClient selectOne(List<SimpleReferenceClient> clients, List<Object> params) {
        if(clients.size() == 1) {
            return clients.get(0);
        }
        int minWeight = Integer.MAX_VALUE;
        int maxWeight = 0;
        for(SimpleReferenceClient simpleReferenceClient : clients) {
            int weight = simpleReferenceClient.getWeight();
            minWeight = Math.min(minWeight, weight);
            maxWeight = Math.max(maxWeight, weight);
        }
        List<SimpleReferenceClient> weightInvokes;
        if(minWeight == maxWeight) {
            weightInvokes = clients;
        } else {
            int value = currentWeight.incrementAndGet() % maxWeight;
            weightInvokes = Lists.newArrayList();
            for(SimpleReferenceClient simpleReferenceClient : clients) {
                int weight = simpleReferenceClient.getWeight();
                if(weight >= value) {
                    weightInvokes.add(simpleReferenceClient);
                }
            }
        }
        return weightInvokes.get(currentWeight.intValue() % weightInvokes.size());
    }


}
