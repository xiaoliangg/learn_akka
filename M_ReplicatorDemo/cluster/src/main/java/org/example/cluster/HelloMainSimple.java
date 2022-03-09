package org.example.cluster;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * 输出分析
 * HelloWorld Actor Paht:akka://hello/user/helloWorld   ////hello为系统名称,user为用户actor，所有的用户actor都会挂载在user路径下,helloWorld表示该actor的名字
 * Greeter Actor Path:akka://hello/user/helloWorld/greeter
 * Hello World!
 * Hello World!
 * // 如下一行表示消息投递失败，失败原因是HelloWorld将自己终止了，导致Greeter发送的消息无法投递
 * [INFO] [01/25/2022 09:53:18.356] [hello-akka.actor.default-dispatcher-5] [akka://hello/user/helloWorld] Message [com.example.Greeter$Msg] from Actor[akka://hello/user/helloWorld#-2083073378] to Actor[akka://hello/user/helloWorld#-2083073378] was not delivered. [1] dead letters encountered. If this is not an expected behavior, then [Actor[akka://hello/user/helloWorld#-2083073378]] may have terminated unexpectedly, This logging can be turned off or adjusted with configuration settings 'akka.log-dead-letters' and 'akka.log-dead-letters-during-shutdown'.
 */

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/25 9:47
 **/
public class HelloMainSimple {
    public static void main(String[] args) {
        String port = args[0];
//        String port = "15033";
        final String connectIP = "127.0.0.1";

        final Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).
                withFallback(ConfigFactory.parseString("akka.remote.netty.tcp.hostname=" + connectIP)).
                withFallback(ConfigFactory.load("samplehello.conf"));
        // ActorSystem 是管理和维护Actor的系统。一个应用程序只需要一个ActorSys1tem就够用了。
        // 参数1表示系统名称。参数2表示配置文件
        ActorSystem system = ActorSystem.create("ClusterSystemTest", config);
        // ActorSystem 创建的Actor为顶级Actor
        ActorRef a = system.actorOf(Props.create(HelloWorld.class),"helloWorld");
        System.out.println("HelloWorld Actor Path:" + a.path());
    }
}
