package org.example.client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.cluster.client.ClusterClient;
import akka.cluster.client.ClusterClientSettings;
import akka.event.LoggingAdapter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;
import org.example.msg.CommonReqMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: TODO
 * @author: xiaoliang
 * @date: 2022/3/9 14:27
 **/
public class AKKAClient {
    private Logger logger = LoggerFactory.getLogger(AKKAClient.class);
    private LoggingAdapter akkaLogger;
    Long wait = 2L;

    private  ActorSystem actorSystem;
    private ActorRef client;

    private static final AKKAClient akkaClient = new AKKAClient();
    public static AKKAClient getInstance(){
        return akkaClient;
    }
    private AKKAClient(){

        logger.info("akka client init...");

        final Config defaultConfig = ConfigFactory.parseString("akka.remote.netty.tcp.port2=" + 333).
                withFallback(ConfigFactory.load("samplehello.conf"));


//        Config defaultConfig = ConfigFactory.load("samplehello.conf");
        int defaultPort = defaultConfig.getInt("akka.remote.netty.tcp.port");
        logger.info("akka client default port "+defaultPort);

        int port = 0;
        port = defaultPort;
        boolean isPortAvailable = false;
        try {
            isPortAvailable = NetUtil.isPortAvailable(port);
        } catch (Exception e) {
            logger.info("akka client default port is inavailable");
        }

        if (!isPortAvailable) {
            // 尝试3次其他端口
            logger.info("akka client try other port...");
            for (int i = 0; i < 3; i++) {
                if (!isPortAvailable) {
                    port += 1;
                    try {
                        isPortAvailable = NetUtil.isPortAvailable(port);
                    } catch (Exception e) {
                    }
                } else {
                    break;
                }
            }
        }

        if (!isPortAvailable) {
            port += 1;
        }

        String ip = NetUtil.getLocalIPList();
        Config config = ConfigFactory
                .parseString("akka.remote.netty.tcp.hostname=" + ip)
                .withFallback(ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port))
                .withFallback(defaultConfig);

        logger.info("akka client final ip " + ip);
        logger.info("akka client final port " + port);

        actorSystem = ActorSystem.create("StrangerActorSystem", config);

        // start client
        ClusterClientSettings clusterClientSettings = ClusterClientSettings.create(actorSystem);
        client = actorSystem.actorOf(ClusterClient.props(clusterClientSettings), "client");

        akkaLogger = actorSystem.log();

        logger.info("akka client started");
    }

    public  void send(String path, CommonReqMsg object) {
        logger.info("send to path {} {}", path, object);
        client.tell(new ClusterClient.Send(path,  object, false),
                ActorRef.noSender());
    }
}
