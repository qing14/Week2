package com.umeng.soexample.week2.presenter;

import com.umeng.soexample.week2.callback.MyCallBack;
import com.umeng.soexample.week2.model.Imodelmpl;
import com.umeng.soexample.week2.view.Iview;

public class Ipresenterlmpl implements Ipresenter{

    Imodelmpl imodelmpl;
    Iview iview;

    public Ipresenterlmpl(Iview iview) {
        imodelmpl=new Imodelmpl();
        this.iview=iview;
    }

    @Override
    public void startRequestData(String string, Class clas) {
        imodelmpl.startRequest(string, clas, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                iview.setData(data);
            }
        });
    }
}
