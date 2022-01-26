package org.example;

import akka.actor.UntypedActor;
import akka.dispatch.Mapper;
import scala.concurrent.Future;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/26 10:17
 **/
public class CounterActor extends UntypedActor {
    Mapper addMapper = new Mapper<Integer,Integer>() {
        @Override
        public Integer apply(Integer i) {
            return i+1;
        }
    };

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof Integer){
            for (int i = 0; i < 10000; i++) {
                // 我希望能知道Future何时结束
                Future<Integer> f = AgentDemo.counterAgent.alter(addMapper);
                AgentDemo.futures.add(f);
            }
            getContext().stop(getSelf());
        }else{
            unhandled(message);
        }
    }
}
