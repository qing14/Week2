package com.umeng.soexample.week2.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.umeng.soexample.week2.R;
import com.umeng.soexample.week2.ScanActivity;

import java.lang.ref.WeakReference;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class FragmentMian extends Fragment {

    private ImageView mImageView;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mian,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent=getActivity().getIntent();
        String name = intent.getStringExtra("name");
        mImageView=view.findViewById(R.id.image_qr);
        button=view.findViewById(R.id.button_qr);
        QRTask qrTask = new QRTask(getContext(), mImageView);
        qrTask.execute(name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ScanActivity.class));
            }
        });

    }

    private void checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},100);
            }
        }else{
            startActivity(new Intent(getActivity(),ScanActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            startActivity(new Intent(getActivity(), ScanActivity.class));
        }
    }

    class QRTask extends AsyncTask<String,Void,Bitmap>{

        private WeakReference<Context> mContext;
        private WeakReference<ImageView> mImageView;

        public QRTask(Context context,ImageView imageView) {
            mContext=new WeakReference<>(context);
            mImageView=new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            if(TextUtils.isEmpty(strings[0])){
                return null;
            }
            int size=200;
            return QRCodeEncoder.syncEncodeQRCode(strings[0],size);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null){
                mImageView.get().setImageBitmap(bitmap);
            }else{
                Toast.makeText(getContext(),"生成失败",Toast.LENGTH_SHORT).show();
            }

        }
    }
}

