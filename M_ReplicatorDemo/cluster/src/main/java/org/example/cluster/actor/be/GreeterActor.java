package org.example.cluster.actor.be;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import org.example.cluster.actor.cache.StudentCacheActor;
import org.example.cluster.msg.GetMsg;
import org.example.msg.CommonReqMsg;
import org.example.msg.GetStudent;
import org.example.msg.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/25 9:28
 **/
public class GreeterActor extends AbstractActor {
    private static final Logger logger = LoggerFactory.getLogger(GreeterActor.class);

    ActorSelection studentCacheActor;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        studentCacheActor = getContext().actorSelection("/user/studentCacheActor");
    }


    public static enum Msg{
        GREET,DONE;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Msg.GREET.getClass(), msg -> {
                    logger.info("Hello World!");
                    // 向消息发送方发送DONE消息
                    getSender().tell(Msg.DONE,getSender());
                })
                // 更新缓存
                .match(Student.class,this::cacheStudentMsg)
                .match(StudentCacheActor.StudentUpdateResponse.class,
                        o -> logger.info("write student cache response:" + o.toString()))
                // 查询缓存
                .match(GetStudent.class,this::getStudentMsg)
                .match(StudentCacheActor.StudentGetResponse.class,
                        o -> logger.info("Get Student Cache Response:" + o.toString()))
                .match(CommonReqMsg.class,
                        msg -> logger.info("greeter receive msg:{}" ,msg.toString()))
                .build();
    }

    private void cacheStudentMsg(Student student) {
        logger.info("start notify studentCacheActor to cache student:{}" ,student.toString());
        studentCacheActor.tell(new StudentCacheActor.StudentUpdateMsg
                        (student.getId(), Stream.of(student).collect(Collectors.toList())),
                        getSelf());

    }

    private void getStudentMsg(GetStudent student) {
        logger.info("start use studentCacheActor to get student cache:{}" ,student.toString());
        studentCacheActor.tell(new GetMsg(student.getId()),getSelf());
    }
}
