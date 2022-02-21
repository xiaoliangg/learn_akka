package org.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.cluster.ddata.*;

import java.time.Duration;
import java.util.Optional;
import akka.cluster.ddata.Replicator.*;

/**
 * @description: updaet:https://doc.akka.io/docs/akka/current/distributed-data.html#update
 * @author: xiaoliang
 * @date: 2022/2/21 10:58
 **/
public class DemonstrateUpdateWithRequestContext extends AbstractActor {
    final SelfUniqueAddress node = DistributedData.get(getContext().getSystem()).selfUniqueAddress();
    final ActorRef replicator = DistributedData.get(getContext().getSystem()).replicator();

    final WriteConsistency writeTwo = new WriteTo(2, Duration.ofSeconds(3));
    final Key<PNCounter> counter1Key = PNCounterKey.create("counter1");

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        String.class,
                        a -> a.equals("increment"),
                        a -> {
                            // incoming command to increase the counter
                            Optional<Object> reqContext = Optional.of(getSender());
                            Replicator.Update<PNCounter> upd =
                                    new Replicator.Update<PNCounter>(
                                            counter1Key,
                                            PNCounter.create(),
                                            writeTwo,
                                            reqContext,
                                            curr -> curr.increment(node, 1));
                            replicator.tell(upd, getSelf());
                        })
                .match(
                        UpdateSuccess.class,
                        a -> a.key().equals(counter1Key),
                        a -> {
                            ActorRef replyTo = (ActorRef) a.getRequest().get();
                            replyTo.tell("ack", getSelf());
                        })
                .match(
                        UpdateTimeout.class,
                        a -> a.key().equals(counter1Key),
                        a -> {
                            ActorRef replyTo = (ActorRef) a.getRequest().get();
                            replyTo.tell("nack", getSelf());
                        })
                .build();
    }
}
