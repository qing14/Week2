package com.umeng.soexample.week2.model;

public class LoginBean<T> {
    String msg;
    int code;
    T data;

    public LoginBean(String msg, int code, T data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public LoginBean() {
    }

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
