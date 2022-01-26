package org.example;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.sun.deploy.net.MessageHeader;
import com.typesafe.config.ConfigFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/25 11:53
 **/
public class Main {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("lifecycle", ConfigFactory.load("lifecycle.conf"));
        customStrategy(system);
    }

    public static void customStrategy(ActorSystem system){
        ActorRef a = system.actorOf(Props.create(Supervisor.class),"Supervisor");
        a.tell(Props.create(RestartActor.class),ActorRef.noSender());

        ActorSelection sel = system.actorSelection("akka://lifecycle/user/Supervisor/restartActor");

        for(int i=0;i<100;i++){
            sel.tell(RestartActor.Msg.RESTART,ActorRef.noSender());
        }

        // 7.6 选择Actor。 支持通配符
        for (int i = 0;i<1000;i++){
            List<ActorRef> works = new ArrayList<>();
            works.add(system.actorOf(Props.create(Supervisor.class),"worker_" + i));
        }
        ActorSelection selection = system.actorSelection("akka://lifecycle/user/worker_*");
        selection.tell("",ActorRef.noSender());

    }
}
