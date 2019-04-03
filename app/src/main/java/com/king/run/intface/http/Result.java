package com.king.run.intface.http;

import java.io.Serializable;

/**
 * 返回数据结构实例
 */

public class Result implements Serializable {
    private String data;
    private String message;
    private int code;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
