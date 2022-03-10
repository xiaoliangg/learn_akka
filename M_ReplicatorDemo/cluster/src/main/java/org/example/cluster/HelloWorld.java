package org.example.cluster;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import org.example.msg.CommonReqMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.Duration;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/25 9:26
 **/
public class HelloWorld extends AbstractActor {
    private Logger logger = LoggerFactory.getLogger(HelloWorld.class);

    ActorRef greeter;

    /**
     * Actor实例创建后，启动前，被Akka框架调用，完成一些初始化的工作
     */
    @Override
    public void preStart(){
        // Akka创建Actor实例
        // 创建Greeter时使用的HelloWorld类的上下文，因此greeter是HelloWorld的子Actor
        greeter = getContext().actorOf(Props.create(Greeter.class),"greeter");
        System.out.println("Greeter Actor Path:" + greeter.path());
        greeter.tell(Greeter.Msg.GREET,getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Greeter.Msg.DONE.getClass(), msg -> {
                    System.out.println("22222222222222222");

//                    greeter.tell(Greeter.Msg.GREET,getSelf());
                    // 终止自己
//                    getContext().stop(getSelf());
                })
                .match(CommonReqMsg.class,msg -> {
                    logger.info("helloWorld msg is:" ,msg.toString());
                    System.out.println("11111111111111111");
                })
                .build();
    }



    private SupervisorStrategy strategy =
            new OneForOneStrategy(-1, Duration.Inf(),
                    DeciderBuilder.matchAny(e -> {
                        logger.error(getSelf().path().toStringWithoutAddress() + "'s child ", e);
                        return SupervisorStrategy.resume();
                    }).build());

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

}
