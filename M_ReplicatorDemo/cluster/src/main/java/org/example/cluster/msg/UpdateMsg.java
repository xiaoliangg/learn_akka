package org.example.cluster.msg;


import lombok.Data;

@Data
public class UpdateMsg {

    private final int id;

    public UpdateMsg(int id) {
        this.id = id;
    }
}
