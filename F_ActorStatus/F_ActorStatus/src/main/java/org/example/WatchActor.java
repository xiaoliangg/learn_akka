package org.example;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @description: <实战Java高并发程序设计>7.4 Actor的生命周期
 * @author: yuliang
 * @date: 2022/1/25 10:37
 **/
public class WatchActor extends UntypedActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public WatchActor(ActorRef ref){
        // getContext含义是获取当前actor的上下文
        getContext().watch(ref);
    }
    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof Terminated){
            System.out.println(String.format("%s has terminated,shutting down system",
                    ((Terminated)message).getActor().path()));
            // todo ActorSystem 如何关闭?
//            getContext().system().shutdown();
        }else{
            unhandled(message);
        }

    }
}
