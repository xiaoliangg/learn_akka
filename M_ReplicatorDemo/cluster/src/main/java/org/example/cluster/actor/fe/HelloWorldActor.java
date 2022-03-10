package org.example.cluster.actor.fe;

import akka.actor.*;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import akka.japi.pf.DeciderBuilder;
import org.example.cluster.actor.be.GreeterActor;
import org.example.msg.CommonReqMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.Duration;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/25 9:26
 **/
public class HelloWorldActor extends AbstractActor {
    private Logger logger = LoggerFactory.getLogger(HelloWorldActor.class);

    ActorRef greeter;

    /**
     * Actor实例创建后，启动前，被Akka框架调用，完成一些初始化的工作
     */
    @Override
    public void preStart(){
        // Akka创建Actor实例
        // 创建Greeter时使用的HelloWorld类的上下文，因此greeter是HelloWorld的子Actor
        greeter = getContext().actorOf(Props.create(GreeterActor.class),"greeter");
        System.out.println("Greeter Actor Path:" + greeter.path());
        // 加入 mediator 后，外部可调用
//        ActorRef mediator = DistributedPubSub.get(getContext().getSystem()).mediator();
//        mediator.tell(new DistributedPubSubMediator.Put(greeter), ActorRef.noSender());

        greeter.tell(GreeterActor.Msg.GREET,getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GreeterActor.Msg.DONE.getClass(), msg -> {
                    System.out.println("22222222222222222");
                    // 终止自己
//                    getContext().stop(getSelf());
                })
                .match(CommonReqMsg.class,msg -> {
                    logger.info("helloWorld msg is:{}" ,msg.toString());
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
