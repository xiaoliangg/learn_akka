package org.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/25 10:46
 **/
public class DeadMain {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("deadwatch", ConfigFactory.load("samplehello.conf"));
        ActorRef worker = system.actorOf(Props.create(MyWorker.class),"worker");
        // Props.create() 第一个参数表示要创建的Actor类型，第二个参数为这个Actor的构造函数的参数。即我们自定义的构造函数 WatchActor(ActorRef ref)
        system.actorOf(Props.create(WatchActor.class,worker),"watcher");
        worker.tell(MyWorker.Msg.WORKING,ActorRef.noSender());
        worker.tell(MyWorker.Msg.DONE,ActorRef.noSender());
        worker.tell(PoisonPill.getInstance(),ActorRef.noSender());
    }
}
