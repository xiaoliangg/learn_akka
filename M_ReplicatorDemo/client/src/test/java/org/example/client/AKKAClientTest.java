package org.example.client;

import org.example.msg.CommonReqMsg;
import org.example.msg.Student;
import org.junit.Test;

public class AKKAClientTest {

    /**
     * 首次测试
     */
    @Test
    public void test1(){
        for (int i = 0; i < 1; i++) {
            CommonReqMsg msg = new CommonReqMsg();
            msg.setMsgId("1871029");
            msg.setEvent(9211 + i);
            msg.setFrom("stranger");
            AKKAClient.getInstance().send("/user/helloWorld",msg);
        }
    }

    @Test
    public void testReplicator(){
        for (int i = 0; i < 1; i++) {
            Student student = new Student();
            student.setMsgId("1234987");
            student.setId(139);
            student.setName("liang");
            AKKAClient.getInstance().send("/user/helloWorld",student);
        }
    }
}