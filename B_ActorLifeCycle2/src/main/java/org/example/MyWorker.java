package org.example;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/25 10:27
 **/
public class MyWorker extends UntypedActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    public static enum Msg{
        WORKING,DONE,CLOSE;
    }

    // 资源初始化
    @Override
    public void preStart(){
        System.out.println("MyWorker is starting");
    }

    // 资源释放
    @Override
    public void postStop(){
        System.out.println("MyWorker is stoppiing");
    }
    @Override
    public void onReceive(Object message) throws Throwable {

        if(message == Msg.WORKING){
            System.out.println("I am working");
        }else if(message == Msg.DONE){
            System.out.println("stop working");
        }else if(message == Msg.CLOSE){
            System.out.println("I will shutdown");
            getSender().tell(Msg.CLOSE,getSelf());
            getContext().stop(getSelf());
        }else{
            unhandled(message);
        }
    }

}
