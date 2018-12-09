package com.umeng.soexample.week2.view;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.umeng.soexample.week2.R;
import com.umeng.soexample.week2.fragment.FragmentFirst;
import com.umeng.soexample.week2.fragment.FragmentMian;

public class ShowActivity extends AppCompatActivity{

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        viewPager=findViewById(R.id.viewpager);
        tabLayout=findViewById(R.id.tab);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            String[] mens=new String[]{"首页","我的"};
            @Override
            public Fragment getItem(int i) {
                switch (i){
                    case 0:return new FragmentFirst();
                    case 1:return new FragmentMian();
                    default:return null;
                }
            }

            @Override
            public int getCount() {
                return mens.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mens[position];
            }
        });
        tabLayout.setupWithViewPager(viewPager);

    }
}
