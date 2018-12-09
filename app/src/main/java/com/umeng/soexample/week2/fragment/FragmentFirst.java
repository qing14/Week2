package com.umeng.soexample.week2.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.soexample.week2.R;
import com.umeng.soexample.week2.adapter.ListAdapter;
import com.umeng.soexample.week2.model.BannerBean;
import com.umeng.soexample.week2.model.NewBean;
import com.umeng.soexample.week2.presenter.Ipresenterlmpl;
import com.umeng.soexample.week2.util.NetUtil;
import com.umeng.soexample.week2.view.Iview;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoaderInterface;


import xlistview.bawei.com.xlistviewlibrary.XListView;

public class FragmentFirst extends Fragment implements Iview {

    private Banner banner;
    private XListView xListView;
    private ListAdapter adapter;
    private Ipresenterlmpl ipresenterlmpl;
    private String imageurl="http://www.xieast.com/api/banner.php";
    private String listUrl="http://www.xieast.com/api/news/news.php?page=";
    private int mpage=1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(),R.layout.fragment_first,null);
      //  inflater.inflate(R.layout.fragment_first,container,false)
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        banner.setImageLoader(new ImageLoaderInterface<ImageView>() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                BannerBean.DataBean bannerBean= (BannerBean.DataBean) path;
                ImageLoader.getInstance().displayImage(bannerBean.getImg(),imageView);
            }

            @Override
            public ImageView createImageView(Context context) {
                ImageView imageView=new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }
        });
        initBannerData();

        xListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mpage=1;
                initListData();
            }

            @Override
            public void onLoadMore() {
                initListData();
            }
        });
        initListData();
    }

    private void initBannerData() {
        new AsyncTask<String, Void, Object>() {
            @Override
            protected Object doInBackground(String... strings) {
                return NetUtil.requestData(imageurl, BannerBean.class);
            }

            @Override
            protected void onPostExecute(Object o) {
                BannerBean bannerBean= (BannerBean)o;
                if(bannerBean.getCode()==1 && bannerBean.getData().size()>0){
                    banner.setImages(((BannerBean) o).getData());
                    banner.start();
                }
            }
        }.execute(imageurl);
    }

    private void initListData() {
        ipresenterlmpl.startRequestData(listUrl+mpage,NewBean.class);
    }

    private void initView(View view) {
        banner=view.findViewById(R.id.banner);
        xListView=view.findViewById(R.id.xlistView);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        ipresenterlmpl=new Ipresenterlmpl(this);
        adapter=new ListAdapter(getContext());
        xListView.setAdapter(adapter);
        xListView.setPullRefreshEnable(true);
        xListView.setPullLoadEnable(true);
    }

    @Override
    public void setData(Object data) {
        NewBean newBean= (NewBean) data;
        if( newBean.getCode()==1 && newBean.getData().size()>0){
            if(mpage==1){
                adapter.setList(newBean.getData());
            }else{
                adapter.addList(newBean.getData());
            }
            xListView.stopLoadMore();
            xListView.stopRefresh();
            mpage++;
        }

    }
}
