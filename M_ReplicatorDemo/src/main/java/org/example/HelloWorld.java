package org.example;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/25 9:26
 **/
public class HelloWorld extends UntypedActor {
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
    public void onReceive(Object message) throws Throwable {
        if(message == Greeter.Msg.DONE){
            greeter.tell(Greeter.Msg.GREET,getSelf());
            // 终止自己
            getContext().stop(getSelf());
        }else{
            unhandled(message);
        }
    }
}
