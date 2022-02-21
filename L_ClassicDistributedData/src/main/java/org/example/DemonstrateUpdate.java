package org.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.cluster.ddata.*;
import akka.japi.pf.ReceiveBuilder;
import akka.cluster.ddata.Replicator.WriteConsistency;
import akka.cluster.ddata.Replicator.WriteTo;
import akka.cluster.ddata.Replicator.WriteMajority;
import akka.cluster.ddata.Replicator.WriteAll;

import java.time.Duration;

/**
 * @description: https://doc.akka.io/docs/akka/current/distributed-data.html#update
 * Update
 * @author: xiaoliang
 * @date: 2022/2/21 10:39
 **/
public class DemonstrateUpdate extends AbstractActor {
    final SelfUniqueAddress node =
            DistributedData.get(getContext().getSystem()).selfUniqueAddress();
    final ActorRef replicator = DistributedData.get(getContext().getSystem()).replicator();

    final Key<PNCounter> counter1Key = PNCounterKey.create("counter1");
    final Key<GSet<String>> set1Key = GSetKey.create("set1");
    final Key<ORSet<String>> set2Key = ORSetKey.create("set2");
    final Key<Flag> activeFlagKey = FlagKey.create("active");

    @Override
    public Receive createReceive() {
        ReceiveBuilder b = receiveBuilder();

        b.matchEquals(
                "demonstrate update",
                msg -> {
                    System.out.println("yl_msg:" + msg);
                    replicator.tell(
                            new Replicator.Update<PNCounter>(
                                    counter1Key,
                                    PNCounter.create(),
                                    Replicator.writeLocal(),
                                    curr -> curr.increment(node, 1)),
                            getSelf());

                    final WriteConsistency writeTo3 = new WriteTo(3, Duration.ofSeconds(1));
                    replicator.tell(
                            new Replicator.Update<GSet<String>>(
                                    set1Key, GSet.create(), writeTo3, curr -> curr.add("hello")),
                            getSelf());

                    final WriteConsistency writeMajority = new WriteMajority(Duration.ofSeconds(5));
                    replicator.tell(
                            new Replicator.Update<ORSet<String>>(
                                    set2Key, ORSet.create(), writeMajority, curr -> curr.add(node, "hello")),
                            getSelf());

                    final WriteConsistency writeAll = new WriteAll(Duration.ofSeconds(5));
                    replicator.tell(
                            new Replicator.Update<Flag>(
                                    activeFlagKey, Flag.create(), writeAll, curr -> curr.switchOn()),
                            getSelf());
                });
        return b.build();
    }
}