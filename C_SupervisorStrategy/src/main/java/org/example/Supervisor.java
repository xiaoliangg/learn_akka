package org.example;

import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.japi.Function;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/25 11:09
 **/
public class Supervisor extends UntypedActor {
    // 1分钟内重试3次，如果超过频率，直接杀死Actor
    private static SupervisorStrategy strategy = new OneForOneStrategy(3, Duration.create(1, TimeUnit.MINUTES),
            new Function<Throwable, SupervisorStrategy.Directive>() {
                @Override
                public SupervisorStrategy.Directive apply(Throwable param) throws Exception {
                    if(param instanceof ArithmeticException){ // 1/0会报该错误
                        System.out.println("meet ArithmeticException,just resume");
                        return SupervisorStrategy.resume(); // 不做任何处理
                    }else if(param instanceof NullPointerException){
                        System.out.println("meet NullPointerException,restart");
                        return SupervisorStrategy.restart(); // 重启Actor
                    }else if(param instanceof IllegalArgumentException){
                        return SupervisorStrategy.stop();  // 停止Actor
                    }else{
                        return SupervisorStrategy.escalate(); // 向上抛出，由顶层的Actor处理
                    }
                }
            });

    /**
     * 覆盖父类方法，设置使用自定义的监督策略
     * @return
     */
    @Override
    public SupervisorStrategy supervisorStrategy(){
        return strategy;
    }
    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof Props){
            // 新建名为restartActor的子Actor,子Actor由当前supervisor监督
            getContext().actorOf((Props)message,"restartActor");
        }else{
            unhandled(message);
        }
    }
}
