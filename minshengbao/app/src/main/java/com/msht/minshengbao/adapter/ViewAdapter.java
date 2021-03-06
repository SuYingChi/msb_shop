package com.msht.minshengbao.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msht.minshengbao.functionActivity.fragment.PayRecord;
import com.msht.minshengbao.functionActivity.fragment.SelfHelpPay;

/**
 * Created by hong on 2016/3/25.
 */
public class ViewAdapter extends FragmentPagerAdapter {
    private String fragments[] = {"自助缴费","缴费记录"};
    private String id;
    private String password;
    private SelfHelpPay mSelfpay;
    private PayRecord mPayrecord;
    public ViewAdapter(FragmentManager fm, Context applicationContext, String userId, String passwords) {
        super(fm);
        this.id=userId;
        this.password=passwords;
    }
    @Override
    public Fragment getItem(int position) {
        Bundle bundle=new Bundle();
        bundle.putString("id",id);
        bundle.putString("password",password);
        switch (position){
            case 0:
                mSelfpay=new SelfHelpPay();
                mSelfpay.setArguments(bundle);
                return mSelfpay;
            case 1:
                mPayrecord=new PayRecord();
                mPayrecord.setArguments(bundle);
                return  mPayrecord;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments[position];
    }
}
