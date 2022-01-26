package org.example;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/25 14:12
 **/
public class Printer extends UntypedActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(),this);

    @Override
    public void onReceive(Object message) throws Throwable {
        log.info("akka.future.Printer.onReceive:" + message);

        if(message instanceof Integer){
            log.info("printer:" + message);
        }else{
            unhandled(message);
        }
    }
}
