package org.example.msg;

import jnr.ffi.annotations.In;

import java.io.Serializable;

/**
 * @description: TODO
 * @author: xiaoliang
 * @date: 2022/3/9 15:34
 **/
public class CommonReqMsg implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3169263166233919489L;
    private String from;
    private String msgId;
    private Integer event;

    public CommonReqMsg(){

    }

    public CommonReqMsg(String from,String msgId, Integer event) {
        this.event = event;
        this.from = from;
        if (event == null) {
            this.msgId = this.from+"_"+this.getClass().getSimpleName() + msgId;
        } else {
            this.msgId = this.from+"_"+event + msgId;
        }
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "CommonReqMsg{" +
                "from='" + from + '\'' +
                ", msgId='" + msgId + '\'' +
                ", event=" + event +
                '}';
    }
}
