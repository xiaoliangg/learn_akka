package org.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.cluster.ddata.DistributedData;
import akka.cluster.ddata.Key;
import akka.cluster.ddata.PNCounter;
import akka.cluster.ddata.PNCounterKey;
import akka.cluster.ddata.Replicator.*;

import java.math.BigInteger;

/**
 * @description: https://doc.akka.io/docs/akka/current/distributed-data.html#subscribe
 * @author: xiaoliang
 * @date: 2022/2/21 11:17
 **/
public class DemonstrateSubscribe extends AbstractActor {
    final ActorRef replicator = DistributedData.get(getContext().getSystem()).replicator();
    final Key<PNCounter> counter1Key = PNCounterKey.create("counter1");

    BigInteger currentValue = BigInteger.valueOf(0);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        Changed.class,
                        a -> a.key().equals(counter1Key),
                        a -> {
                            Changed<PNCounter> g = a;
                            currentValue = g.dataValue().getValue();
                        })
                .match(
                        String.class,
                        a -> a.equals("get-count"),
                        a -> {
                            // incoming request to retrieve current value of the counter
                            getSender().tell(currentValue, getSender());
                        })
                .build();
    }

    @Override
    public void preStart() {
        // subscribe to changes of the Counter1Key value
        replicator.tell(new Subscribe<PNCounter>(counter1Key, getSelf()), ActorRef.noSender());
    }
}
