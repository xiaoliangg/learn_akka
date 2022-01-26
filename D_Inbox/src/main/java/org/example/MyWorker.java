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

    public enum Msg{
        WORKING,DONE,CLOSE;
    }
    @Override
    public void onReceive(Object message) throws Throwable {
        if(message == Msg.WORKING){
            log.info("I am working");
        }else if(message == Msg.DONE){
            log.info("stop working");
        }else if(message == Msg.CLOSE){
            log.info("I will shutdown");
            getSender().tell(Msg.CLOSE,getSelf());
            getContext().stop(getSelf());
        }else{
            unhandled(message);
        }
    }
}
