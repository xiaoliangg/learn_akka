package org.example;

import akka.actor.*;
import akka.pattern.Patterns;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @description:《实战Java高并发程序设计》7.10询问模式:Actor中的Future  todo 不理解！！
 * @author: yuliang
 * @date: 2022/1/25 14:17
 **/
public class Main {
    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("askdemo", ConfigFactory.load("samplehello.conf"));
        ActorRef worker = system.actorOf(Props.create(MyWorker.class),"worker");
        ActorRef printer = system.actorOf(Props.create(Printer.class),"printer");

        system.actorOf(Props.create(WatchActor.class,worker),"watcher");

        // 等待Future返回 todo 以下两个超时时间的含义不理解
//        Future<Object> f = Patterns.ask(worker,5,6500);
//        Future<Object> f = Patterns.ask(worker,5,1500);
        // 超时时间大于和小于上行代码超时时间时，超时报错不一样。 可能原因:没有超时，但是 MyWorker没有返回值
//        int result = (int) Await.result(f,Duration.create(10,TimeUnit.SECONDS));
//        int result = (int) Await.result(f,Duration.create(6,TimeUnit.SECONDS));
//        System.out.println("result:" + result);

        // 不等待返回值，直接重定向到其他actor，有返回值来的时候将会重定向到printActor
        Future<Object> f2 = Patterns.ask(worker,8,3000);
        Patterns.pipe(f2,system.dispatcher()).to(printer);

        worker.tell(PoisonPill.getInstance(),ActorRef.noSender());

    }
}
