package org.example.client.test;


import org.example.client.AKKAClient;
import org.example.msg.CommonReqMsg;
import org.example.msg.GetStudent;
import org.example.msg.Student;

/**
 * @description: TODO
 * @author: xiaoliang
 * @date: 2022/3/9 17:21
 **/
public class TestReplicator {
    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            Student student = new Student();
            student.setMsgId("1234987");
            student.setId(139);
            student.setName("liang");
            AKKAClient.getInstance().send("/user/helloWorld",student);
        }
        //获取
        GetStudent student = new GetStudent();
        student.setMsgId("100000111");
        student.setId(139);
        AKKAClient.getInstance().send("/user/helloWorld",student);
    }
}
