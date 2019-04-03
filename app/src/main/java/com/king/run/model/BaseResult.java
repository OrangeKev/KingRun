package com.king.run.model;

import java.io.Serializable;

/**
 * 作者：shuizi_wade on 2017/10/17 14:49
 * 邮箱：674618016@qq.com
 */
public class BaseResult implements Serializable{
    private String msg;
    private int code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
