package org.example.cluster.actor.fe;

import akka.actor.*;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import akka.japi.pf.DeciderBuilder;
import org.example.cluster.actor.be.GreeterActor;
import org.example.msg.CommonReqMsg;
import org.example.msg.GetStudent;
import org.example.msg.Student;
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
        logger.info("Greeter Actor Path:" + greeter.path());
        // 加入 DistributedPubSubMediator 后，外部可调用
//        ActorRef mediator = DistributedPubSub.get(getContext().getSystem()).mediator();
//        mediator.tell(new DistributedPubSubMediator.Put(greeter), ActorRef.noSender());

        greeter.tell(GreeterActor.Msg.GREET,getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GreeterActor.Msg.DONE.getClass(), msg -> {
                    logger.info("greeter telling me he has finished it");
                    // 终止自己
//                    getContext().stop(getSelf());
                })
                .match(Student.class, msg -> {
                    logger.info("helloWorld Student msg is:{}" ,msg.toString());
                    greeter.tell(msg,getSelf());
                })
                .match(GetStudent.class, msg -> {
                    logger.info("helloWorld GetStudent msg is:{}" ,msg.toString());
                    greeter.tell(msg,getSelf());
                })
                .match(CommonReqMsg.class,msg -> {
                    logger.info("helloWorld CommonReqMsg msg is:{}" ,msg.toString());
                    logger.info("11111111111111111");
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

    @Override
    public void postStop() throws Exception {
        logger.info(this.getClass().getName() + " postStop");
        super.postStop();
    }

}
