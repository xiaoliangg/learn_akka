package org.example;

import akka.actor.*;
import akka.agent.Agent;
import akka.dispatch.Futures;
import akka.dispatch.OnComplete;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/26 10:21
 **/
public class AgentDemo {
    public static Agent<Integer> counterAgent = Agent.create(0, ExecutionContext.global());
    static ConcurrentLinkedQueue<Future<Integer>> futures = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) throws TimeoutException {
        final ActorSystem system = ActorSystem.create("agentdemo", ConfigFactory.load("samplehello.conf"));
        ActorRef[] counter = new ActorRef[10];
        for (int i = 0; i < counter.length; i++) {
            counter[i] = system.actorOf(Props.create(CounterActor.class),"counter_" + i);
        }

        final Inbox inbox = Inbox.create(system);
        for (int i = 0; i < counter.length; i++) {
            inbox.send(counter[i],1);
            inbox.watch(counter[i]);
        }
        int closeCount = 0;
        // 等待所有Actor全部结束
        while(true){
            Object msg = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
            if(msg instanceof Terminated){
                closeCount++;
                if(closeCount == counter.length){
                    break;
                }
            }else{
                System.out.println(msg);
            }
        }
        // 等待所有的累加线程完成，因为它们都是异步的
        // yl Futures.sequence 的作用:当所有的Future都完成后，才会返回一个Future，这个Future的结果就是所有的Future的结果的集合。
        Futures.sequence(futures,system.dispatcher()).onComplete(
                new OnComplete<Iterable<Integer>>() {
                    @Override
                    public void onComplete(Throwable failure, Iterable<Integer> success) throws Throwable {
                        System.out.println("counterAgent:" + counterAgent.get());
                        // todo 如何关闭
//                        system.shutdown();
                    }
                },system.dispatcher()
        );
    }

}
