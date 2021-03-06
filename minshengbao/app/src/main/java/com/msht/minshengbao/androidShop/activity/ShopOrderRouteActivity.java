package com.msht.minshengbao.androidShop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.RefundAllFormGoodListAdapter;
import com.msht.minshengbao.androidShop.adapter.ShopRouteAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.OrderRouteBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.viewInterface.ISearchDeliverView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShopOrderRouteActivity extends ShopBaseActivity implements ISearchDeliverView {
    @BindView(R.id.state)
    TextView tvState;
    @BindView(R.id.company)
    TextView tvCompany;
    @BindView(R.id.sn)
    TextView tvSn;
    @BindView(R.id.phone)
    TextView tvPhone;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView ivback;
    private String orderId;
    private OrderRouteBean bean;
    private List<String> dataList=new ArrayList<String>();
    private ShopRouteAdapter adapter;

    @Override
    protected void setLayout() {
        setContentView(R.layout.route);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true).navigationBarColor(R.color.black).navigationBarWithKitkatEnable(false).init();
        ImmersionBar.setTitleBar(this, mToolbar);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderId = getIntent().getStringExtra("id");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        rcl.setNestedScrollingEnabled(false);
        rcl.setLayoutManager(linearLayoutManager);
        adapter = new ShopRouteAdapter(this);
        adapter.setDatas(dataList);
        rcl.setAdapter(adapter);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ShopPresenter.getOrderRoute(this);
    }

    @Override
    public String getOrderid() {
        return orderId;
    }

    @Override
    public void onSearchDeliverSuccess(String s) {
       bean = JsonUtil.toBean(s,OrderRouteBean.class);
        if (bean != null) {
            dataList.addAll(bean.getDatas().getDeliver_info());
            adapter.notifyDataSetChanged();
            tvState.setText(bean.getDatas().getState());
            tvPhone.setText(bean.getDatas().getExpress_service_phone());
            tvCompany.setText(bean.getDatas().getExpress_name());
            tvSn.setText(bean.getDatas().getShipping_code());
        }
    }
}
