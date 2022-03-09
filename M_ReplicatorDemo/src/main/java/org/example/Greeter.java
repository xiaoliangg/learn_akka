package org.example;

import akka.actor.AbstractActor;
import akka.actor.PoisonPill;
import akka.actor.UntypedActor;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/25 9:28
 **/
public class Greeter extends AbstractActor {


    public static enum Msg{
        GREET,DONE;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Msg.GREET.getClass(), msg -> {
                    System.out.println("Hello World!");
                    // 向消息发送方发送DONE消息
                    getSender().tell(Msg.DONE,getSender());
                })
                .build();
    }
}
