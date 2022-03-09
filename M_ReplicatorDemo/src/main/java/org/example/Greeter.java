package org.example;

import akka.actor.PoisonPill;
import akka.actor.UntypedActor;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/25 9:28
 **/
public class Greeter extends UntypedActor {
    public static enum Msg{
        GREET,DONE;
    }
    @Override
    public void onReceive(Object message) throws Throwable {
        if(message == Msg.GREET){
            System.out.println("Hello World!");
            // 向消息发送方发送DONE消息
            getSender().tell(Msg.DONE,getSender());
        }else{
            unhandled(message);
        }
    }
}
