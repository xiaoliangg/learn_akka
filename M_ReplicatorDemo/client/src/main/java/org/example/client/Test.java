package org.example.client;


import org.example.msg.CommonReqMsg;

/**
 * @description: TODO
 * @author: xiaoliang
 * @date: 2022/3/9 17:21
 **/
public class Test {
    public static void main(String[] args) {
        CommonReqMsg msg = new CommonReqMsg();
        msg.setMsgId("1871029");
        msg.setEvent(9211);
        msg.setFrom("stranger");
        AKKAClient.getInstance().send("/user/helloWorld",msg);
    }
}
