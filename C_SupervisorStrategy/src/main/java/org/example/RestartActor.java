package org.example;

import akka.actor.UntypedActor;
import scala.Option;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/25 11:29
 **/
public class RestartActor extends UntypedActor {

    public enum Msg{
        DONE,RESTART
    }

    @Override
    public void preStart(){
        System.out.println("preStart hashcode:" + this.hashCode());
    }

    @Override
    public void postStop(){
        System.out.println("postStop hashcode:" + this.hashCode());
    }

    @Override
    public void postRestart(Throwable reason)throws Exception{
        super.postRestart(reason);
        System.out.println("postRestart hashcode:" + this.hashCode());
    }

    @Override
    public void preRestart(Throwable reason, Option opt)throws Exception{
        System.out.println("preRestart hashcode:" + this.hashCode());
    }
    @Override
    public void onReceive(Object message) throws Throwable {
        if(message == Msg.DONE){
            getContext().stop(getSelf());
        }else if(message == Msg.RESTART){
            System.out.println(((Object)null).toString());
            double a = 0/0;
        }else{
            unhandled(message);
        }
    }
}
