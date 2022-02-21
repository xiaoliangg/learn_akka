package org.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.cluster.ddata.*;
import akka.cluster.ddata.Replicator.*;

import java.time.Duration;

/**
 * @description: https://doc.akka.io/docs/akka/current/distributed-data.html#delete
 * @author: xiaoliang
 * @date: 2022/2/21 11:26
 **/
public class DemonstrateDelete extends AbstractActor {
    final ActorRef replicator = DistributedData.get(getContext().getSystem()).replicator();

    final Key<PNCounter> counter1Key = PNCounterKey.create("counter1");
    final Key<ORSet<String>> set2Key = ORSetKey.create("set2");

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals(
                        "demonstrate delete",
                        msg -> {
                            replicator.tell(
                                    new Delete<PNCounter>(counter1Key, Replicator.writeLocal()), getSelf());

                            final WriteConsistency writeMajority = new WriteMajority(Duration.ofSeconds(5));
                            replicator.tell(new Delete<PNCounter>(counter1Key, writeMajority), getSelf());
                        })
                .build();
    }
}
