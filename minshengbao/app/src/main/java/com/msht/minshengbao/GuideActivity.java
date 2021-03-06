package com.msht.minshengbao;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.msht.minshengbao.adapter.GuideViewPagerAdapter;
import com.msht.minshengbao.functionActivity.MainActivity;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager mVp;
    // 引导页图片资源
    private static final int[] PICS = { R.layout.guide_view1,
            R.layout.guide_view2, R.layout.guide_view3,R.layout.guide_view4};
    private ImageView[] dots;
    // 记录当前选中位置
    private int currentIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        List<View> views = new ArrayList<View>();
        for (int i = 0; i < PICS.length; i++) {
            View view = LayoutInflater.from(this).inflate(PICS[i], null);
            if (i == PICS.length - 1) {
                Button startBtn = (Button) view.findViewById(R.id.btn_enter);
                startBtn.setTag("enter");
                startBtn.setOnClickListener(this);
            }
            views.add(view);
        }
        mVp = (ViewPager) findViewById(R.id.vp_guide);
        GuideViewPagerAdapter mAdapter= new GuideViewPagerAdapter(views);
        mVp.setAdapter(mAdapter);
        mVp.addOnPageChangeListener(new PageChangeListener());
        initDots();
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[PICS.length];
        // 循环取得小点图片
        for (int i = 0; i < PICS.length; i++) {
            // 得到一个LinearLayout下面的每一个子元素
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(false);
            dots[i].setOnClickListener(this);
            // 设置位置tag，方便取出与当前位置对应
            dots[i].setTag(i);
        }
        currentIndex = 0;
        // 设置为白色，即选中状态
        dots[currentIndex].setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("enter")) {
            enterMainActivity();
            return;
        }
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }
    private void enterMainActivity() {
        Intent intent = new Intent(GuideActivity.this,
                MainActivity.class);
        startActivity(intent);
        SharedPreferencesUtil.putBoolean(GuideActivity.this, SharedPreferencesUtil.FIRST_OPEN, false);
        finish();
    }
    private void setCurDot(int position) {
        if (position < 0 || position > PICS.length || currentIndex == position) {
            return;
        }
        dots[position].setEnabled(true);
        dots[currentIndex].setEnabled(false);
        currentIndex = position;
    }
    private void setCurView(int position) {
        if (position < 0 || position >= PICS.length) {
            return;
        }
        mVp.setCurrentItem(position);
    }
    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {
            setCurDot(position);
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
