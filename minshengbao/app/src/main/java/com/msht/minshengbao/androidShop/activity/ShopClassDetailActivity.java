package com.msht.minshengbao.androidShop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.Fragment.GoodFragment;
import com.msht.minshengbao.androidShop.adapter.ClassDetailLeftAdapter;
import com.msht.minshengbao.androidShop.adapter.ClassDetailRightAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.PopRclAdapterHaveHeadView;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.customerview.AddCarOrBuyGoodDialog;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.AddCarBean;
import com.msht.minshengbao.androidShop.shopBean.ClassDetailRightBean;
import com.msht.minshengbao.androidShop.shopBean.ComfirmShopGoodBean;
import com.msht.minshengbao.androidShop.shopBean.GuiGeBean;
import com.msht.minshengbao.androidShop.shopBean.MyClassListBean;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeClassBean;
import com.msht.minshengbao.androidShop.shopBean.SimpleCarBean;
import com.msht.minshengbao.androidShop.util.AddViewHolder;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.ICarListView;
import com.msht.minshengbao.androidShop.viewInterface.IModifyCarGoodNumView;
import com.msht.minshengbao.androidShop.viewInterface.IShopClassDetailView;
import com.msht.minshengbao.androidShop.viewInterface.IShopGoodDetailView;
import com.msht.minshengbao.events.CarNumEvent;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class ShopClassDetailActivity extends ShopBaseActivity implements IShopClassDetailView, OnRefreshLoadMoreListener, OnRefreshListener, ClassDetailRightAdapter.AddCarListener, IShopGoodDetailView, ICarListView, IModifyCarGoodNumView {
    @BindView(R.id.rcl)
    RecyclerView rclLeft;
    @BindView(R.id.rcl2)
    RecyclerView rclRight;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.search)
    ImageView ivSearch;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView ivback;
    @BindView(R.id.menu)
    ImageView ivMenu;
    @BindView(R.id.shop_car_num)
    TextView tvCarNum;
    @BindView(R.id.triangle)
    ImageView triangle;
    @BindView(R.id.rlt_title)
    RelativeLayout rltTitle;
    @BindView(R.id.iv_no_data)
    ImageView ivNoOrder;
    private String gcId;
    private ClassDetailLeftAdapter classDetailLeftAdapter;
    private ArrayList<MyClassListBean> list;
    private String rightGcId;
    private int rightCurrenPage = 1;
    private ClassDetailRightAdapter classDetailRightAdapter;
    private List<ClassDetailRightBean.DatasBean.GoodsListBean> rightDataList = new ArrayList<ClassDetailRightBean.DatasBean.GoodsListBean>();
    private int selectPosition;
    private PopupWindow popWindow;
    private PopRclAdapterHaveHeadView rcl3Adapter;
    private AddViewHolder popViewHolder;
    //private int carNum;
    private AddCarOrBuyGoodDialog addCarOrBuyGoodDialog;
    private int selectNum = 1;
    private String goodName;
    private String goodPrice;
    private String remianNum;
    private String goodImageUrl;
    private String goodId;
    private String isPickup_self;
    private String storeName;
    private String storeId;
    private String guigename;
    private List<GuiGeBean> guigeList = new ArrayList<>();
    private int selectedGuigePosition = 0;
    private List<SimpleCarBean> caridlist = new ArrayList<SimpleCarBean>();
    private int carNum=-1;
    private String carid;


    @Override
    protected void setLayout() {
        setContentView(R.layout.class_detail);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true).navigationBarColor(R.color.black).navigationBarWithKitkatEnable(false).init();
        ImmersionBar.setTitleBar(this, mToolbar);
    }

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CarNumEvent messageEvent) {
        carNum = messageEvent.getCarNum();
        if (carNum > 0) {
            tvCarNum.setVisibility(View.VISIBLE);
            tvCarNum.setText(String.format("%d", carNum));
        } else {
            tvCarNum.setVisibility(View.GONE);
        }
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*EventBus.getDefault().register(this);
        carNum = getIntent().getIntExtra("carnum", -1);
        if (carNum > 0) {
            tvCarNum.setVisibility(View.VISIBLE);
            tvCarNum.setText(String.format("%d", carNum));
        } else {
            tvCarNum.setVisibility(View.GONE);
        }*/
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gcId = getIntent().getStringExtra("data");
        String tit = getIntent().getStringExtra("title");
        selectPosition = getIntent().getIntExtra("selectPosition", 0);
        tvTitle.setText(tit);
        rltTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUtil.showPopWindow(ShopClassDetailActivity.this, tvTitle, false, popViewHolder.getCustomView(), popWindow);
                rotateImageView(180);
            }
        });
        Bundle bundle = getIntent().getExtras();
        //初始化顶部点击弹出popwindow
        if (bundle != null) {
            List<ShopHomeClassBean.ClassBean.ItemBean> popDatas = (List<ShopHomeClassBean.ClassBean.ItemBean>) bundle.getSerializable("list");
            final ArrayList<MyClassListBean> popWindowList = new ArrayList<MyClassListBean>();
            MyClassListBean myClassListBean;
            String data;
            for (ShopHomeClassBean.ClassBean.ItemBean classListBean : popDatas) {
                myClassListBean = new MyClassListBean();
                myClassListBean.setGc_name(classListBean.getTitle());
                data = classListBean.getData();
                int index = data.indexOf("id=");
                data = data.substring(index + 3).trim();
                myClassListBean.setGc_id(data);
                if (popDatas.indexOf(classListBean) == selectPosition) {
                    myClassListBean.setIsSelected(true);
                } else {
                    myClassListBean.setIsSelected(false);
                }
                popWindowList.add(myClassListBean);
            }
            popViewHolder = new AddViewHolder(this, R.layout.layout_popupwindow);
            RecyclerView rcl3 = popViewHolder.getView(R.id.rcl3);
            rcl3.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            rcl3.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            rcl3Adapter = new PopRclAdapterHaveHeadView(this);
            rcl3Adapter.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (selectPosition != position) {
                        selectPosition = position;
                        for (int i = 0; i < popWindowList.size(); i++) {
                            MyClassListBean bean = popWindowList.get(i);
                            if (i == position) {
                                bean.setIsSelected(true);
                            } else {
                                bean.setIsSelected(false);
                            }
                            popWindowList.set(i, bean);
                        }
                        rcl3Adapter.notifyDataSetChanged();
                        popWindow.dismiss();
                        tvTitle.setText(popWindowList.get(position).getGc_name());
                        gcId = popWindowList.get(position).getGc_id();
                        ShopPresenter.getClassDetailLeft(ShopClassDetailActivity.this);
                    }
                }
            });
            rcl3.setAdapter(rcl3Adapter);
            rcl3Adapter.setDatas(popWindowList);
            LogUtils.e("popWindowList.size()==================" + popWindowList.size());
            rcl3Adapter.notifyDataSetChanged();
            popWindow = new PopupWindow(popViewHolder.getCustomView(), ViewGroup.LayoutParams.MATCH_PARENT, DimenUtil.dip2px(400));
            popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    rotateImageView(0);
                }
            });
        }
        ShopPresenter.getClassDetailLeft(this);
        rclLeft.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rclLeft.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        classDetailLeftAdapter = new ClassDetailLeftAdapter(this);
        classDetailLeftAdapter.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (!TextUtils.equals(list.get(position).getGc_id(), rightGcId)) {
                    rightGcId = list.get(position).getGc_id();
                    for (int i = 0; i < list.size(); i++) {
                        MyClassListBean bean = list.get(i);
                        if (i == position) {
                            bean.setIsSelected(true);
                        } else {
                            bean.setIsSelected(false);
                        }
                        list.set(i, bean);
                    }
                    classDetailLeftAdapter.notifyDataSetChanged();
                    rightCurrenPage = 1;
                    ShopPresenter.getClassDetailRight(ShopClassDetailActivity.this);
                }
            }
        });
        rclLeft.setAdapter(classDetailLeftAdapter);

        rclRight.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rclRight.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        classDetailRightAdapter = new ClassDetailRightAdapter(this);
        classDetailRightAdapter.setOnAddCarListener(this);
        classDetailRightAdapter.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ClassDetailRightBean.DatasBean.GoodsListBean item = rightDataList.get(position);
                HashMap<String, String> map = new HashMap<String, String>();
                String goodsId = item.getGoods_id();
                map.put("type", "goods");
                map.put("data", goodsId);
                doNotAdClick(map);
            }
        });
        rclRight.setAdapter(classDetailRightAdapter);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopClassDetailActivity.this, ShopSearchActivty.class);
                intent.putExtra("main", 1);
                startActivity(intent);
            }
        });
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(getKey())) {
                    startActivity(new Intent(ShopClassDetailActivity.this, NoLoginCarActivity.class));
                } else if (carNum == 0) {
                    startActivity(new Intent(ShopClassDetailActivity.this, NoCarActivity.class));
                } else if (carNum > 0){
                    startActivity(new Intent(ShopClassDetailActivity.this, ShopCarActivity.class));
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ShopPresenter.getCarList(this, false);
    }

    @Override
    public void onLeftSuccess(ArrayList<MyClassListBean> list) {
        this.list = list;
        classDetailLeftAdapter.setDatas(list);
        rightGcId = list.get(0).getGc_id();
        classDetailLeftAdapter.notifyDataSetChanged();
        ShopPresenter.getClassDetailRight(this);
    }

    @Override
    public String getGcId() {
        return gcId;
    }

    @Override
    public String getRightGcId() {
        return rightGcId;
    }

    @Override
    public String getRightCurrenPage() {
        return 10 + "";
    }

    @Override
    public String getCurrenPage() {
        return rightCurrenPage + "";
    }

    @Override
    public void onRightRclSuccess(List<ClassDetailRightBean.DatasBean.GoodsListBean> list, int page_total) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        if (page_total == 0) {
            rightDataList.clear();
            refreshLayout.setEnableAutoLoadMore(true);
            refreshLayout.setNoMoreData(false);
            classDetailRightAdapter.notifyDataSetChanged();
            ivNoOrder.setVisibility(View.VISIBLE);
        } else {
            ivNoOrder.setVisibility(View.INVISIBLE);
            if (rightCurrenPage == 1) {
                rightDataList.clear();
                rightDataList.addAll(list);
                classDetailRightAdapter.setDatas(rightDataList);
                classDetailRightAdapter.notifyDataSetChanged();
            } else if (rightCurrenPage >= page_total) {
                refreshLayout.setEnableAutoLoadMore(false);
                refreshLayout.finishLoadMoreWithNoMoreData();
                refreshLayout.setNoMoreData(true);
            } else {
                refreshLayout.setEnableAutoLoadMore(true);
                refreshLayout.setNoMoreData(false);
                if (list.size() != 0) {
                    rightDataList.addAll(list);
                    classDetailRightAdapter.setDatas(rightDataList);
                    classDetailRightAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onError(String s) {
        super.onError(s);
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        rightCurrenPage++;
        ShopPresenter.getClassDetailRight(this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        rightCurrenPage = 1;
        ShopPresenter.getClassDetailRight(this);
    }

    @Override
    public void addCar(ClassDetailRightBean.DatasBean.GoodsListBean goods) {
        goodId = goods.getGoods_id();
        goodPrice = goods.getGoods_price();
        goodImageUrl = goods.getGoods_image_url();
        goodName = goods.getGoods_name();
        remianNum = goods.getGoods_storage();
        storeId = goods.getStore_id();
        storeName = goods.getStore_name();
        selectedGuigePosition = 0;
        ShopPresenter.getGoodDetail(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onGetGoodDetailSuccess(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject datas = jsonObject.getJSONObject("datas");
            JSONObject goods_info = datas.getJSONObject("goods_info");
            JSONObject guigenameobj = goods_info.optJSONObject("spec_name");
            JSONObject spec_valueobj = goods_info.optJSONObject("spec_value");
            JSONObject spec_listObj = datas.optJSONObject("spec_list");
            isPickup_self = goods_info.optString("pickup_self");
            List<String> guigekey = JsonUtil.getJsonObjectKeyList(guigenameobj);
            if (guigekey.size() > 0) {
                String guigenamekey = guigekey.get(0);
                JSONObject guigevalueobj = spec_valueobj.optJSONObject(guigenamekey);
                guigename = guigenameobj.optString(guigenamekey);
                List<String> guigeidlist = JsonUtil.getJsonObjectKeyList(guigevalueobj);
                guigeList.clear();
                for (int i = 0; i < guigeidlist.size(); i++) {
                    String id = guigeidlist.get(i);
                    String guigegoodid = spec_listObj.optString(id);
                    String guigeName = guigevalueobj.optString(guigeidlist.get(i));
                    if (i == selectedGuigePosition) {
                        guigeList.add(new GuiGeBean(id, guigeName, guigegoodid, true));
                    } else {
                        guigeList.add(new GuiGeBean(id, guigeName, guigegoodid, false));
                    }
                }
            } else {
                guigename = "";
                guigeList.clear();
            }
            if (!isFinishing() && addCarOrBuyGoodDialog != null && addCarOrBuyGoodDialog.isShowing()) {
                addCarOrBuyGoodDialog.refreshData();
            } else {
                addCarOrBuyGoodDialog = new AddCarOrBuyGoodDialog(this, this);
                addCarOrBuyGoodDialog.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getGoodsid() {
        return goodId;
    }

    @Override
    public void addCar() {
        if (TextUtils.isEmpty(getKey())) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            for(SimpleCarBean bean:caridlist){
                if(bean.getGoods_id().equals(goodId)){
                    carid = bean.getCart_id();
                    ShopPresenter.modifyGoodNum(this,this);
                    return;
                }
            }
            ShopPresenter.addCar(this);
        }
    }

    @Override
    public void buyGood() {
        if (!TextUtils.isEmpty(getKey())) {
            List<ComfirmShopGoodBean> list = new ArrayList<ComfirmShopGoodBean>();
            List<ComfirmShopGoodBean.GoodsBean> list2 = new ArrayList<ComfirmShopGoodBean.GoodsBean>();
            list2.add(new ComfirmShopGoodBean.GoodsBean(storeName, storeId, goodImageUrl, goodName, selectNum + "", goodPrice, goodId));
            ComfirmShopGoodBean comfirmShopGoodBean = new ComfirmShopGoodBean();
            comfirmShopGoodBean.setStore_id(storeId);
            comfirmShopGoodBean.setStore_name(storeName);
            comfirmShopGoodBean.setGoods(list2);
            list.add(comfirmShopGoodBean);
            if (!TextUtils.isEmpty(isPickup_self)) {
                if (TextUtils.equals(isPickup_self, "0")) {
                    Intent intent = new Intent(this, ShopComfirmOrdersActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ifCar", "0");
                    bundle.putString("isPickup_self", isPickup_self);
                    bundle.putSerializable("data", (Serializable) list);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    PopUtil.toastInBottom("暂不支持自提商品购买");
                }
            }
        } else {
            new Intent(this, LoginActivity.class);
        }
    }

    @Override
    public int getSelectedGoodNum() {
        return selectNum;
    }

    @Override
    public void setSelectedGoodNum(int num) {
        selectNum = num;
    }

    @Override
    public String getNameDialog() {
        return goodName;
    }

    @Override
    public String getPrice() {
        return goodPrice;
    }

    @Override
    public String getRemainNum() {
        return remianNum;
    }

    @Override
    public String getImageUrl() {
        return goodImageUrl;
    }

    @Override
    public void onAddCarSuccess(String s) {
        AddCarBean bean = JsonUtil.toBean(s, AddCarBean.class);
        if (TextUtils.equals(bean.getDatas(), "1")) {
            PopUtil.showAutoDissHookDialog(this, "添加购物车成功", 100);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(getKey())) {
                        ShopPresenter.getCarList(ShopClassDetailActivity.this, false);
                    }
                }
            }, 1600);
        }
    }

    @Override
    public String getCarId() {
        return carid;
    }

    @Override
    public String getCarItemNum() {
        return selectNum+"";
    }

    @Override
    public void onModifyGoodNumSuccess(String s) {
        PopUtil.showAutoDissHookDialog(this, "购物车数量修改成功", 100);
        ShopPresenter.getCarList(this, false);
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public String getGuigeName() {
        return guigename;
    }

    @Override
    public List<GuiGeBean> getGuigeList() {
        return guigeList;
    }

    @Override
    public void onSelectGoodId(int childposition) {
        if (childposition != selectedGuigePosition) {
            selectedGuigePosition = childposition;
            goodId = guigeList.get(childposition).getGuigeGoodId();
            ShopPresenter.getGoodDetail(this);
        }
    }


    private void rotateImageView(int retate) {
        triangle.setPivotX(triangle.getWidth() / 2);
        triangle.setPivotY(triangle.getHeight() / 2);//支点在图片中心
        triangle.setRotation(retate);
    }

    @Override
    public void onGetCarListSuccess(String s) {
        try {
            JSONObject jsonObj = new JSONObject(s);
            JSONObject dataObj = jsonObj.getJSONObject("datas");
            JSONArray jsonArray = dataObj.optJSONArray("cart_list");
            carNum = 0;
            caridlist.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray good = jsonArray.optJSONObject(i).optJSONArray("goods");
                for (int ii = 0; ii < good.length(); ii++) {
                    carNum += Integer.valueOf(good.optJSONObject(ii).optString("goods_num"));
                    caridlist.add(new SimpleCarBean(good.optJSONObject(ii).optString("goods_id"), good.optJSONObject(ii).optString("cart_id")));

                }
            }
            if (carNum > 0) {
                tvCarNum.setVisibility(View.VISIBLE);
                tvCarNum.setText(String.format("%d", carNum));
            } else {
                tvCarNum.setVisibility(View.GONE);
            }
            EventBus.getDefault().postSticky(new CarNumEvent(carNum));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
