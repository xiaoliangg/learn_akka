package org.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.cluster.ddata.*;
import akka.japi.pf.ReceiveBuilder;
import akka.cluster.ddata.Replicator.*;

import java.time.Duration;

/**
 * @description: https://doc.akka.io/docs/akka/current/distributed-data.html#get
 * @author: xiaoliang
 * @date: 2022/2/21 11:07
 **/
public class DemonstrateGet extends AbstractActor {
    final ActorRef replicator = DistributedData.get(getContext().getSystem()).replicator();

    final Key<PNCounter> counter1Key = PNCounterKey.create("counter1");
    final Key<GSet<String>> set1Key = GSetKey.create("set1");
    final Key<ORSet<String>> set2Key = ORSetKey.create("set2");
    final Key<Flag> activeFlagKey = FlagKey.create("active");

    @Override
    public Receive createReceive() {
        ReceiveBuilder b = receiveBuilder();

        b.matchEquals(
                "demonstrate get",
                msg -> {
                    replicator.tell(
                            new Replicator.Get<PNCounter>(counter1Key, Replicator.readLocal()), getSelf());

                    final ReadConsistency readFrom3 = new ReadFrom(3, Duration.ofSeconds(1));
                    replicator.tell(new Replicator.Get<GSet<String>>(set1Key, readFrom3), getSelf());

                    final ReadConsistency readMajority = new ReadMajority(Duration.ofSeconds(5));
                    replicator.tell(new Replicator.Get<ORSet<String>>(set2Key, readMajority), getSelf());

                    final ReadConsistency readAll = new ReadAll(Duration.ofSeconds(5));
                    replicator.tell(new Replicator.Get<Flag>(activeFlagKey, readAll), getSelf());
                });
        return b.build();
    }
}
