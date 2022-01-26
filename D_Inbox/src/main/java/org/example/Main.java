package org.example;

import akka.actor.*;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @description:《实战Java高并发程序设计》7.7消息收件箱
 * @author: yuliang
 * @date: 2022/1/25 14:17
 **/
public class Main {
    public static void main(String[] args) throws TimeoutException {
        ActorSystem system = ActorSystem.create("inboxdemo", ConfigFactory.load("samplehello.conf"));
        ActorRef worker = system.actorOf(Props.create(MyWorker.class),"worker");

        final Inbox inbox = Inbox.create(system);
        inbox.watch(worker);
        inbox.send(worker, MyWorker.Msg.WORKING);
        inbox.send(worker, MyWorker.Msg.DONE);
        inbox.send(worker, MyWorker.Msg.CLOSE);

        while(true){
            Object msg = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
            if(msg == MyWorker.Msg.CLOSE){
                System.out.println("My Worker is closing");
            }else if(msg instanceof Terminated){
                System.out.println("My Worker is dead");
                // todo 如何关闭 ActorSystem？
//                system.shutdown();
                break;
            }else{
                System.out.println(msg);
            }
        }
    }
}
