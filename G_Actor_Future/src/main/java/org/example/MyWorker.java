package org.example;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/25 14:12
 **/
public class MyWorker extends UntypedActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(),this);

    @Override
    public void onReceive(Object message) throws Throwable {
        log.info("akka.future.MyWorker.onReceive:" + message);
        Thread.sleep(1000);
        if(message instanceof Integer){
            log.info("MyWorker:" + message);
            getSender().tell("这是一个响应",self());
        }else{
            unhandled(message);
        }
    }
}
