package org.example.cluster.msg;

public class UpdateResponse {

    public final static Integer SUCCESS = 1;
    public final static Integer TIMEOUT = 2;
    public final static Integer FAILURE = 3;

    private final int id;
    private final Integer status;

    public int getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public UpdateResponse(int id, Integer status) {
        this.id = id;
        this.status = status;
    }

    @Override
    public String toString() {
        return "UpdateResponse{" +
                "id=" + id +
                ", status=" + status +
                '}';
    }
}
