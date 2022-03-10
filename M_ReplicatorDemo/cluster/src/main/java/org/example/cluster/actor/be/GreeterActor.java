package org.example.cluster.actor.be;

import akka.actor.AbstractActor;
import org.example.msg.CommonReqMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/25 9:28
 **/
public class GreeterActor extends AbstractActor {
    private Logger logger = LoggerFactory.getLogger(GreeterActor.class);


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
                .match(CommonReqMsg.class, msg -> {
                    logger.info("greeter receive msg:{}" ,msg.toString());
                })
                .build();
    }
}
