package com.umeng.soexample.week2.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.umeng.soexample.week2.callback.MyCallBack;
import com.umeng.soexample.week2.util.NetUtil;

public class Imodelmpl implements Imodel {
    @SuppressLint("StaticFieldLeak")
    @Override
    public void startRequest(final String string, final Class clas, final MyCallBack myCallBack) {
        new AsyncTask<String, Void, Object>() {
            @Override
            protected Object doInBackground(String... strings) {
                return NetUtil.requestData(string, clas);
            }

            @Override
            protected void onPostExecute(Object o) {
                myCallBack.onSuccess(o);
            }
        }.execute(string);

    }
}
