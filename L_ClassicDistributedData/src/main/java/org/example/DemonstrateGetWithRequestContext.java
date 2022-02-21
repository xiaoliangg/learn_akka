package org.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.cluster.ddata.*;
import akka.cluster.ddata.Replicator.*;

import java.time.Duration;
import java.util.Optional;

/**
 * @description: https://doc.akka.io/docs/akka/current/distributed-data.html#get
 * @author: xiaoliang
 * @date: 2022/2/21 11:11
 **/
public class DemonstrateGetWithRequestContext extends AbstractActor {
    final ActorRef replicator = DistributedData.get(getContext().getSystem()).replicator();

    final ReadConsistency readTwo = new ReadFrom(2, Duration.ofSeconds(3));
    final Key<PNCounter> counter1Key = PNCounterKey.create("counter1");

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        String.class,
                        a -> a.equals("get-count"),
                        a -> {
                            // incoming request to retrieve current value of the counter
                            Optional<Object> reqContext = Optional.of(getSender());
                            replicator.tell(new Replicator.Get<PNCounter>(counter1Key, readTwo), getSelf());
                        })
                .match(
                        GetSuccess.class,
                        a -> a.key().equals(counter1Key),
                        a -> {
                            ActorRef replyTo = (ActorRef) a.getRequest().get();
                            GetSuccess<PNCounter> g = a;
                            long value = g.dataValue().getValue().longValue();
                            replyTo.tell(value, getSelf());
                        })
                .match(
                        GetFailure.class,
                        a -> a.key().equals(counter1Key),
                        a -> {
                            ActorRef replyTo = (ActorRef) a.getRequest().get();
                            replyTo.tell(-1L, getSelf());
                        })
                .match(
                        NotFound.class,
                        a -> a.key().equals(counter1Key),
                        a -> {
                            ActorRef replyTo = (ActorRef) a.getRequest().get();
                            replyTo.tell(0L, getSelf());
                        })
                .build();
    }
}
