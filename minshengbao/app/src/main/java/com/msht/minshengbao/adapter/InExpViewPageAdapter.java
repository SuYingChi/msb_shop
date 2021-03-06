package com.msht.minshengbao.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.msht.minshengbao.functionActivity.fragment.IncomeExpenseFragment;

/**
 * Created by hong on 2017/6/13.
 */
public class InExpViewPageAdapter extends FragmentPagerAdapter {
    private String fragments[] = {"全部","收入","支出"};
    public InExpViewPageAdapter(FragmentManager fm, Context applicationContext) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        return IncomeExpenseFragment.getinstance(position);
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
