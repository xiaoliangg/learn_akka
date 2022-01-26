package org.example;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;

/**
 * @description: 7.9 Actor的内置状态转换
 * @author: yuliang
 * @date: 2022/1/25 15:20
 * 解释:首次发送消息时，内置状态既不是angry也不是happy,消息由onReceive函数接收，并置状态。后续消息，由对应状态的Procedure处理，不再由 onReceive 处理
 **/
public class BabyActor extends UntypedActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(),this);

    public static enum Msg{
        SLEEP,PLAY,CLOSE;
    }

    Procedure<Object> angry = new Procedure<Object>() {
        @Override
        public void apply(Object param) throws Exception {
            System.out.println("angryApply:" + param);
            if(param == Msg.SLEEP){
                getSender().tell("I am already angry",getSelf());
                System.out.println("I am already angry");
            }else if(param == Msg.PLAY){
                System.out.println("I like playing");
                getContext().become(happy);
            }
        }
    };

    Procedure<Object> happy = new Procedure<Object>() {
        @Override
        public void apply(Object param) throws Exception {
            System.out.println("happyApply:" + param);
            if(param == Msg.PLAY){
                getSender().tell("I am already happy",getSelf());
                System.out.println("I am already happy");
            }else if(param == Msg.SLEEP){
                System.out.println("I don't want to sleep");
                getContext().become(angry);
            }
        }
    };



    @Override
    public void onReceive(Object message) throws Throwable {
        System.out.println("onReceive:" + message);
        if(message == Msg.SLEEP){
            getContext().become(angry);
        }else if(message == Msg.PLAY){
            getContext().become(happy);
        }else {
            unhandled(message);
        }

    }
}
