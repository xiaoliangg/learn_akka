package org.example.client;

import org.example.msg.CommonReqMsg;
import org.junit.Test;

public class AKKAClientTest {

    @Test
    public void test1(){
        for (int i = 0; i < 1; i++) {
            CommonReqMsg msg = new CommonReqMsg();
            msg.setMsgId("1871029");
            msg.setEvent(i);
            msg.setFrom("stranger");
            AKKAClient.getInstance().send("user/helloWorld",msg);
        }
    }
}