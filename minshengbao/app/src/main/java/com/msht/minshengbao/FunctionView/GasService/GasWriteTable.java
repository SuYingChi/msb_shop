package com.msht.minshengbao.FunctionView.GasService;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.Adapter.ViewPageWriteTable;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;


public class GasWriteTable extends AppCompatActivity {
    private ViewPagerIndicator indicator;
    private ViewPager mviewPager;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_write_table);
        mContext=this;
        StatusBarCompat.setStatusBar(this);
        PushAgent.getInstance(mContext).onAppStart();   //推送统计
        initHeader();
        initView();
        initEvent();
    }
    private void initHeader() {
        ((TextView)findViewById(R.id.tv_navigation)).setText("自助抄表") ;
        findViewById(R.id.id_goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initView() {
        indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        mviewPager=(ViewPager)findViewById(R.id.id_viewPager_record);
    }
    private void initEvent() {
        mviewPager.setAdapter(new ViewPageWriteTable(getSupportFragmentManager(), getApplicationContext()));
        indicator.setViewPager(mviewPager,0);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
        //ZhugeSDK.getInstance().init(getApplicationContext());
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}
