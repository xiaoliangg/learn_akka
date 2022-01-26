package org.example;


import akka.dispatch.ExecutionContexts;
import sun.management.Agent;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/25 15:01
 **/
public class RouteMain {
    public static Agent<Boolean> flag = Agent.create(true, ExecutionContexts.global());
}
