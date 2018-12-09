package com.umeng.soexample.week2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.soexample.week2.R;
import com.umeng.soexample.week2.model.LoginBean;
import com.umeng.soexample.week2.presenter.Ipresenterlmpl;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,Iview {

    private EditText name,pwd;
    private Ipresenterlmpl ipresenterlmpl;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CheckBox remeber,autologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ipresenterlmpl=new Ipresenterlmpl(this);
        name=findViewById(R.id.name);
        pwd=findViewById(R.id.pwd);
        remeber=findViewById(R.id.remeber);
        autologin=findViewById(R.id.autologin);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.regin).setOnClickListener(this);
        findViewById(R.id.image_qq).setOnClickListener(this);
        sharedPreferences=getSharedPreferences("Data",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        boolean check_l = sharedPreferences.getBoolean("check_l", false);
        boolean check_r = sharedPreferences.getBoolean("check_r", false);
        boolean qq_login = sharedPreferences.getBoolean("qq_login", false);
        if(check_l){
            String names = sharedPreferences.getString("name", null);
            String pwds = sharedPreferences.getString("pwd", null);
            ipresenterlmpl.startRequestData("http://120.27.23.105/user/login?mobile="+names+"&password="+pwds,LoginBean.class);
        }else{
            editor.remove("check_l");
        }
        if(qq_login){
            String names = sharedPreferences.getString("names", null);
            Intent intent=new Intent(MainActivity.this,ShowActivity.class);
            intent.putExtra("name",names);
            startActivity(intent);
        }
        if(check_r){
            String names = sharedPreferences.getString("name", null);
            String pwds = sharedPreferences.getString("pwd", null);
            name.setText(names);
            pwd.setText(pwds);
            remeber.setChecked(true);
        }
        autologin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    remeber.setChecked(true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                String names = name.getText().toString();
                String pwds= pwd.getText().toString();
                if(autologin.isChecked()){
                    editor.putBoolean("check_l",true);
                    editor.putBoolean("check_r",true);
                    editor.putString("name",names);
                    editor.putString("pwd",pwds);
                    editor.commit();
                }
                if(remeber.isChecked()){
                    editor.putBoolean("check_r",true);
                    editor.putString("name",names);
                    editor.putString("pwd",pwds);
                    editor.commit();
                }
                ipresenterlmpl.startRequestData("http://120.27.23.105/user/login?mobile="+names+"&password="+pwds,LoginBean.class);
                break;
            case R.id.regin:
                Intent intent=new Intent(this,ReginActivity.class);
                startActivity(intent);
                break;
            case R.id.image_qq:
                UMShareAPI umShareAPI=UMShareAPI.get(this);
                umShareAPI.getPlatformInfo(this, SHARE_MEDIA.QQ, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        if(!map.toString().equals("")){
                            editor.putBoolean("qq_login",true);
							editor.putString("qq_name",map.get("screen_name"));
							editor.commit();
                            Intent intent=new Intent(MainActivity.this,ShowActivity.class);
                            Log.i("TEST",map.toString());
                            intent.putExtra("name",map.get("screen_name"));
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });
                break;
            default:break;
        }
    }

    @Override
    public void setData(Object data) {
        LoginBean loginBean= (LoginBean) data;
        Toast.makeText(this,((LoginBean) data).getMsg(),Toast.LENGTH_SHORT).show();
        if(loginBean.getCode()==0){
            Intent intent=new Intent(this,ShowActivity.class);
            intent.putExtra("name",name.getText().toString());
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }
}
