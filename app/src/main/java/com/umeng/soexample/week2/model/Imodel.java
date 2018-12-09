package com.umeng.soexample.week2.model;

import com.umeng.soexample.week2.callback.MyCallBack;

public interface Imodel {
    void startRequest(String string, Class clas, MyCallBack myCallBack);
}
