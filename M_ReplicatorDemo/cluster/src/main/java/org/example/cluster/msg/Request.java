package org.example.cluster.msg;

import akka.actor.ActorRef;

public class Request {

    public final int id;
    public final ActorRef replyTo;

    public Request(int id, ActorRef replyTo) {
        this.id = id;
        this.replyTo = replyTo;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", replyTo=" + replyTo +
                '}';
    }
}
