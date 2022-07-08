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
        log.info("我的printer:" + message);

        if(message instanceof Integer){
            log.info("打印消息:" + message);
        }else{
            unhandled(message);
        }
    }
}
