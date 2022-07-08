package org.example.cluster.actor.cache;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.cluster.Cluster;
import akka.cluster.ddata.*;
import org.example.cluster.msg.*;
import org.example.msg.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.Optional;

import static akka.cluster.ddata.Replicator.readLocal;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @description:
 * @author: yuliang
 * @date: 2022/1/25 9:28
 **/
public class StudentCacheActor extends AbstractActor {
    private Logger logger = LoggerFactory.getLogger(StudentCacheActor.class);

    final ActorRef replicator = DistributedData.get(getContext().getSystem()).replicator();
    final Cluster node = Cluster.get(context().system());

    final Replicator.WriteConsistency writeAll = new Replicator.WriteAll(Duration.create(10, SECONDS));
    final Replicator.ReadConsistency readLocal = readLocal();

    private Key<LWWMap<Integer, List<Student>>> studentKey(Integer id) {
        return LWWMapKey.create("studentKey-" + id);
    }

    @Override
    public Receive createReceive() {
        return matchGet()
                .orElse(matchUpdate());
    }

    private Receive matchGet() {
        return receiveBuilder()
                .match(GetMsg.class, this::receiveGetMsg)
                .match(Replicator.GetSuccess.class, this::receiveGetSuccess)
//                .match(Replicator.NotFound.class, this::receiveNotFound)
//                .match(Replicator.GetFailure.class, this::receiveGetFailure)
                .build();
    }

    private Receive matchUpdate() {
        return receiveBuilder()
                .match(StudentUpdateMsg.class, this::receiveUpdateMsg)
                .match(Replicator.UpdateSuccess.class, this::receiveUpdateSuccess)
//                .match(Replicator.UpdateTimeout.class, this::receiveUpdateTimeout)
//                .match(Replicator.UpdateFailure.class, this::receiveUpdateFailure)
                .build();
    }

    private void receiveUpdateMsg(StudentUpdateMsg msg) {
        logger.info(Thread.currentThread().getStackTrace()[1].getClassName()
                + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
                + ":" + msg.toString());
        Optional<Object> ctx = Optional.of(new Request(msg.getId(), sender()));
        // LWWMap 用于 1.存储本地数据  2.与其他节点同步数据
        Replicator.Update<LWWMap<Integer,List<Student>>> update =
                new Replicator.Update<>(
                        // key: Key[A]  studentKey(msg.getId()) 返回 Key<LWWMap<Integer, List<Student>>> 类型的key，
                        // 后面 getMsg方法可以使用此key作为查询条件，去缓存中查询到匹配的缓存，
                        studentKey(msg.getId()),
                        // initial: A   初始化 LWWMap，首次会创建，后续使用之前创建好的
                        LWWMap.create(),
                        // writeConsistency: WriteConsistency   //写入策略，写入所有节点
                        writeAll,
                        // request: Optional[Any]。携带 msg.id,sender()
                        ctx,
                        // 1. curr 为 LWWMap<Integer, List<Student>> 类型，此参数为函数接口,用于缓存，LWWMap.put(集群节点,key,value)。
                        // 2. 其他:观察studentKey()的返回值,可发现 curr的类型为key的泛型类型。
                        curr -> curr.put(node, msg.getId(), msg.getData()));

        // 会回调 receiveUpdateSuccess() 或 receiveUpdateFailure()
        replicator.tell(update, self());
    }

    private void receiveUpdateSuccess(Replicator.UpdateSuccess<LWWMap<Integer, List<Student>>> u) {
        Request req = (Request) u.getRequest().get();
        logger.info(Thread.currentThread().getStackTrace()[1].getClassName()
                + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
                + ":" + req);
        req.replyTo.tell(new StudentUpdateResponse(req.id, StudentUpdateResponse.SUCCESS), self());
    }

    private void receiveGetMsg(GetMsg msg) {
        logger.info(Thread.currentThread().getStackTrace()[1].getClassName()
                + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
                + msg);
        Optional<Object> ctx = Optional.of(new Request(msg.getId(), sender()));
        Replicator.Get<LWWMap<Integer, List<Student>>> get = new Replicator.Get<>(studentKey(msg.getId()), readLocal, ctx);
        replicator.tell(get, self());
    }

    private void receiveGetSuccess(Replicator.GetSuccess<LWWMap<Integer, List<Student>>> g) {
        Request req = (Request) g.getRequest().get();
        Option<List<Student>> valueOption = g.dataValue().get(req.id);
        Optional<List<Student>> valueOptional = Optional.ofNullable(valueOption.isDefined() ? valueOption.get() : null);
        logger.info(Thread.currentThread().getStackTrace()[1].getClassName()
                + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
                + valueOptional.get().stream().toArray());
        req.replyTo.tell(new StudentGetResponse(req.id, valueOptional), self());
    }


    public static class StudentGetResponse extends GetResponse {

        private final Optional<List<Student>> data;

        public StudentGetResponse(Integer id, Optional<List<Student>> data) {
            super(id);
            this.data = data;
        }

        public Optional<List<Student>> getData() {
            return data;
        }

        @Override
        public String toString() {
            return "StudentGetResponse{" +
                    "data=" + data +
                    '}';
        }
    }

    public static class StudentUpdateMsg extends UpdateMsg {
        private final List<Student> data;

        public StudentUpdateMsg(int id, List<Student> data) {
            super(id);
            this.data = data;
        }

        public List<Student> getData() {
            return data;
        }
    }

    public static class StudentUpdateResponse extends UpdateResponse {
        public StudentUpdateResponse(Integer id, Integer status) {
            super(id, status);
        }
    }
}
