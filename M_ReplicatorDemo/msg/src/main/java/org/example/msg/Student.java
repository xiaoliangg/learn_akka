package org.example.msg;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: TODO
 * @author: xiaoliang
 * @date: 2022/3/9 15:34
 **/
@Data
public class Student extends CommonReqMsg {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
}
