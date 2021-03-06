package com.msht.minshengbao.functionActivity.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.adapter.MyFunctionAdapter;
import com.msht.minshengbao.androidShop.ShopConstants;
import com.msht.minshengbao.androidShop.activity.MyShopOrderActivity;
import com.msht.minshengbao.androidShop.activity.ShopCollectionActivity;
import com.msht.minshengbao.androidShop.activity.ShopFootprintActivity;
import com.msht.minshengbao.androidShop.customerview.LoadingDialog;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.RefunBean;
import com.msht.minshengbao.androidShop.shopBean.RefunGoodBean;
import com.msht.minshengbao.androidShop.shopBean.ShopCellectionBean;
import com.msht.minshengbao.androidShop.shopBean.ShopFootPrintBean;
import com.msht.minshengbao.androidShop.util.AppUtil;
import com.msht.minshengbao.androidShop.util.DataStringCallback;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.androidShop.viewInterface.IBaseView;
import com.msht.minshengbao.androidShop.viewInterface.IShopOrdersNumView;
import com.msht.minshengbao.functionActivity.GasService.GasServerOrderActivity;
import com.msht.minshengbao.functionActivity.Invoice.InvoiceOpen;
import com.msht.minshengbao.functionActivity.MessageCenterActivity;
import com.msht.minshengbao.functionActivity.MyActivity.AddressManageActivity;
import com.msht.minshengbao.functionActivity.MyActivity.ConsultRecommend;
import com.msht.minshengbao.functionActivity.MyActivity.CustomerNoManage;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;
import com.msht.minshengbao.functionActivity.MyActivity.MoreSetting;
import com.msht.minshengbao.functionActivity.MyActivity.Mysetting;
import com.msht.minshengbao.functionActivity.MyActivity.MyWalletActivity;
import com.msht.minshengbao.functionActivity.MyActivity.ShareMenuActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ACache;
import com.msht.minshengbao.Utils.CallPhoneUtil;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.ButtonUI.MenuItemM;
import com.msht.minshengbao.ViewUI.CircleImageView;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.msht.minshengbao.ViewUI.widget.MyScrollview;
import com.umeng.analytics.MobclickAgent;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 *
 * @author hong
 * @date 2016/7/2  
 */
public class LoginMyFrag extends Fragment implements View.OnClickListener, MyScrollview.ScrollViewListener, IBaseView, IShopOrdersNumView {
    private MyScrollview myScrollview;
    private LinearLayout layoutNavigation;
    private RelativeLayout layoutMySetting;
    private CircleImageView circleImageView;
    private TextView tvNavigation;
    private TextView tvNickname;
    private MenuItemM btnMessage;
    private String avatarUrl;
    private String nickname;
    private Bitmap myAvatar = null;
    private ACache mCache;
    private int bgHeight;
    private Context mContext;
    /**
     * 最大显示消息数
     **/
    private static final int MAX_MASSAGE = 99;
    private MyNoScrollGridView mGridView;
    private MyFunctionAdapter mAdapter;
    private ArrayList<HashMap<String, Integer>> mList = new ArrayList<HashMap<String, Integer>>();
    private final String mPageName = "首页_个人中心";
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private final GetImageHandler getImageHandler = new GetImageHandler(this);
    private LoadingDialog centerLoadingDialog;
    private int waitEveluateOrdersNum;
    private TextView tvWaitEveluate;
    private TextView tvWaitGet;
    private TextView tvWaitPay;
    private TextView tvAllOrder;
    private int waitGetOrdersNum;
    private int waitPayOrdersNum;
    private int allOrdersNum;
    private TextView tvRrfundOrder;
    private int refundOrderNum = -1;
    private int refundGoodOrderNum = -1;
    private LinearLayout llShopOrder;
    private LinearLayout llrefund;
    private LinearLayout llwaitEveluate;
    private LinearLayout llwaitget;
    private LinearLayout llwaitPay;
    private LinearLayout llCollect;
    private LinearLayout llFootprint;
    private TextView tvCollect;
    private TextView tvFootprint;
    private int collectNum;

    public LoginMyFrag() {
    }

    @Override
    public void showLoading() {

        if (!isDetached() && centerLoadingDialog == null) {
            centerLoadingDialog = new LoadingDialog(getContext());
            centerLoadingDialog.show();
        } else if (!isDetached() && !centerLoadingDialog.isShowing()) {
            centerLoadingDialog.show();
        }

    }

    @Override
    public void dismissLoading() {
        if (centerLoadingDialog != null && centerLoadingDialog.isShowing() && !isDetached()) {
            centerLoadingDialog.dismiss();
        }
    }

    @Override
    public void onError(String s) {

    }

    @Override
    public String getKey() {
        return ShopSharePreferenceUtil.getInstance().getKey();
    }

    @Override
    public String getUserId() {
        return ShopSharePreferenceUtil.getInstance().getUserId();
    }

    @Override
    public String getLoginPassword() {
        return ShopSharePreferenceUtil.getInstance().getPassword();
    }

    @Override
    public void onLogout() {
        AppUtil.logout();
    }

    @Override
    public void onNetError() {

    }

    private static class GetImageHandler extends Handler {
        private WeakReference<LoginMyFrag> mWeakReference;

        public GetImageHandler(LoginMyFrag loginMyFrag) {
            mWeakReference = new WeakReference<LoginMyFrag>(loginMyFrag);
        }

        @Override
        public void handleMessage(Message msg) {
            final LoginMyFrag reference = mWeakReference.get();
            // the referenced object has been cleared
            if (reference == null || reference.isDetached()) {
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    reference.myAvatar = (Bitmap) msg.obj;
                    if (reference.myAvatar == null) {
                        reference.circleImageView.setImageResource(R.drawable.potrait);
                    } else {
                        reference.circleImageView.setImageBitmap(reference.myAvatar);
                        reference.mCache.put("avatarimg", reference.myAvatar);
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(reference.mContext, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loginafter_my, container, false);
        mContext = getActivity();
        mCache = ACache.get(mContext);
        avatarUrl = SharedPreferencesUtil.getAvatarUrl(mContext, SharedPreferencesUtil.AvatarUrl, "");
        nickname = SharedPreferencesUtil.getNickName(mContext, SharedPreferencesUtil.NickName, "");
        VariableUtil.loginStatus = SharedPreferencesUtil.getLstate(mContext, SharedPreferencesUtil.Lstate, false);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            view.findViewById(R.id.id_view).setVisibility(View.GONE);
        }
        initView(view);
        myAvatar = mCache.getAsBitmap("avatarimg");
        if (myAvatar != null) {
            circleImageView.setImageBitmap(myAvatar);
        } else {
            if (avatarUrl != null && (!TextUtils.isEmpty(avatarUrl))) {
                onGetAvatar();
            }
        }
        initListeners();
        mAdapter = new MyFunctionAdapter(mContext, mList);
        mGridView.setAdapter(mAdapter);
        initData();
        goActivity();
        getOrdersNum();
        return view;
    }


    public void getOrdersNum() {
        ShopPresenter.getShopOrdersList(this, "state_new", new DataStringCallback(this) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    onGetWaitPayOrdersNum(s);
                }
            }
        });
        ShopPresenter.getShopOrdersList(this, "state_send", new DataStringCallback(this) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    onGetWaitGetOrdersNum(s);
                }
            }
        });
        ShopPresenter.getShopOrdersList(this, "state_noeval", new DataStringCallback(this) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    onGetWaitEveluateOrdersNum(s);
                }
            }
        });
        ShopPresenter.getRefundGoodList(this, new DataStringCallback(this) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    onGetRefundGoodOrdersNum(s);
                }
            }
        });
        ShopPresenter.getRefundMoneyList(this, new DataStringCallback(this) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    onGetRefundMoneyOrdersNum(s);
                }
            }
        });
        ShopPresenter.getShopOrdersList(this, "", new DataStringCallback(this) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    onGetAllOrdersNum(s);
                }
            }
        });
        ShopPresenter.getCollectList(this, new DataStringCallback(this) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    onGetCollectNum(s);
                }
            }
        });
        ShopPresenter.getFootprintList(this, new DataStringCallback(this) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    onGetfootPrintNum(s);
                }
            }
        });
    }

    private void onGetfootPrintNum(String s) {
        ShopFootPrintBean bean = JsonUtil.toBean(s, ShopFootPrintBean.class);
        int footprintNum = bean.getDatas().getGoodsbrowse_list().size();
        if (footprintNum == 0) {
            tvFootprint.setVisibility(View.GONE);
        } else {
            footprintNum = 0;
            tvFootprint.setVisibility(View.VISIBLE);
            List<ShopFootPrintBean.DatasBean.GoodsbrowseListBean> list = bean.getDatas().getGoodsbrowse_list();
            for (ShopFootPrintBean.DatasBean.GoodsbrowseListBean beanb : list) {
                footprintNum += beanb.getGoods_list().size();
            }
            tvFootprint.setText(footprintNum + "");
        }
        llFootprint.setClickable(true);
    }

    private void onGetCollectNum(String s) {
        ShopCellectionBean bean = JsonUtil.toBean(s, ShopCellectionBean.class);
        collectNum = bean.getDatas().getFavorites_list().size();
        if (collectNum == 0) {
            tvCollect.setVisibility(View.GONE);
        } else {
            tvCollect.setVisibility(View.VISIBLE);
            tvCollect.setText(collectNum + "");
        }
        llCollect.setClickable(true);
    }

    private void onGetRefundMoneyOrdersNum(String s) {
        RefunBean bean = JsonUtil.toBean(s, RefunBean.class);
        refundOrderNum = bean.getDatas().getRefund_list().size();
        if (refundGoodOrderNum != -1) {
            if (refundOrderNum + refundGoodOrderNum == 0) {
                tvRrfundOrder.setVisibility(View.GONE);
            } else {
                tvRrfundOrder.setVisibility(View.VISIBLE);
                tvRrfundOrder.setText(String.format("%d", refundOrderNum + refundGoodOrderNum));
            }
            llrefund.setClickable(true);
        }

    }

    private void onGetRefundGoodOrdersNum(String s) {
        RefunGoodBean bean = JsonUtil.toBean(s, RefunGoodBean.class);
        refundGoodOrderNum = bean.getDatas().getReturn_list().size();
        if (refundOrderNum != -1) {
            if (refundOrderNum + refundGoodOrderNum == 0) {
                tvRrfundOrder.setVisibility(View.GONE);
            } else {
                tvRrfundOrder.setVisibility(View.VISIBLE);
                tvRrfundOrder.setText(String.format("%d", refundOrderNum + refundGoodOrderNum));
            }
            llrefund.setClickable(true);
        }
    }

    private void onGetWaitEveluateOrdersNum(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            JSONObject datas = obj.optJSONObject("datas");
            JSONArray order_group_list = datas.optJSONArray("order_group_list");
            waitEveluateOrdersNum = order_group_list.length();
            if (waitEveluateOrdersNum == 0) {
                tvWaitEveluate.setVisibility(View.GONE);
            } else {
                tvWaitEveluate.setVisibility(View.VISIBLE);
                tvWaitEveluate.setText(String.format("%d", waitEveluateOrdersNum));
            }
            llwaitEveluate.setClickable(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void onGetWaitGetOrdersNum(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            JSONObject datas = obj.optJSONObject("datas");
            JSONArray order_group_list = datas.optJSONArray("order_group_list");
            waitGetOrdersNum = order_group_list.length();
            if (waitGetOrdersNum == 0) {
                tvWaitGet.setVisibility(View.GONE);
            } else {
                tvWaitGet.setVisibility(View.VISIBLE);
                tvWaitGet.setText(String.format("%d", waitGetOrdersNum));
            }
            llwaitget.setClickable(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onGetWaitPayOrdersNum(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            JSONObject datas = obj.optJSONObject("datas");
            JSONArray order_group_list = datas.optJSONArray("order_group_list");
            waitPayOrdersNum = order_group_list.length();
            if (waitPayOrdersNum == 0) {
                tvWaitPay.setVisibility(View.GONE);
            } else {
                tvWaitPay.setVisibility(View.VISIBLE);
                tvWaitPay.setText(String.format("%d", waitPayOrdersNum));
            }
            llwaitPay.setClickable(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onGetAllOrdersNum(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            JSONObject datas = obj.optJSONObject("datas");
            JSONArray order_group_list = datas.optJSONArray("order_group_list");
            allOrdersNum = order_group_list.length();
            if (allOrdersNum == 0) {
                tvAllOrder.setVisibility(View.GONE);
            } else {
                tvAllOrder.setVisibility(View.VISIBLE);
                tvAllOrder.setText(String.format("%d", allOrdersNum));
            }
            llShopOrder.setClickable(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView(View view) {
        myScrollview = (MyScrollview) view.findViewById(R.id.id_scrollview);
        mGridView = (MyNoScrollGridView) view.findViewById(R.id.id_function_view);
        layoutNavigation = (LinearLayout) view.findViewById(R.id.id_li_navigation);
        layoutMySetting = (RelativeLayout) view.findViewById(R.id.id_re_gosetting);
        layoutMySetting.setOnClickListener(this);
        btnMessage = (MenuItemM) view.findViewById(R.id.id_mim_message);
        view.findViewById(R.id.id_re_hotline).setOnClickListener(this);
        view.findViewById(R.id.id_re_consult).setOnClickListener(this);
        view.findViewById(R.id.id_re_setting).setOnClickListener(this);
        view.findViewById(R.id.id_right_massage).setOnClickListener(this);
        circleImageView = (CircleImageView) view.findViewById(R.id.id_potrait);
        tvNavigation = (TextView) view.findViewById(R.id.id_tv_naviga);
        tvNickname = (TextView) view.findViewById(R.id.id_nickname);
        tvNickname.setText(nickname);
        onUnReadMessage();
        btnMessage.setOnClickListener(new MenuItemM.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMessageCenter();
            }
        });
        llCollect = (LinearLayout) view.findViewById(R.id.ll_collect);
        tvCollect = (TextView) view.findViewById(R.id.collected_num);
        tvFootprint = (TextView) view.findViewById(R.id.footprint_num);
        llCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShopCollectionActivity.class));
            }
        });
        llFootprint = (LinearLayout) view.findViewById(R.id.ll_footprint);
        llFootprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShopFootprintActivity.class));
            }
        });
        llShopOrder = (LinearLayout) view.findViewById(R.id.shop_order);
        llShopOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyShopOrderActivity.class);
                intent.putExtra("index", 0);
                intent.putExtra("indexChild", 0);
                getActivity().startActivity(intent);
            }
        });
        llwaitPay = (LinearLayout) view.findViewById(R.id.my_wait_pay);
        llwaitPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyShopOrderActivity.class);
                intent.putExtra("index", 0);
                intent.putExtra("indexChild", 1);
                getActivity().startActivity(intent);
            }
        });
        llwaitget = (LinearLayout) view.findViewById(R.id.my_wait_get);
        llwaitget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyShopOrderActivity.class);
                intent.putExtra("index", 0);
                intent.putExtra("indexChild", 3);
                getActivity().startActivity(intent);
            }
        });
        llwaitEveluate = (LinearLayout) view.findViewById(R.id.my_wait_eveluate);
        llwaitEveluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyShopOrderActivity.class);
                intent.putExtra("index", 0);
                intent.putExtra("indexChild", 4);
                getActivity().startActivity(intent);
            }
        });
        llrefund = (LinearLayout) view.findViewById(R.id.my_refund);
        llrefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyShopOrderActivity.class);
                intent.putExtra("index", 1);
                intent.putExtra("indexChild", 0);
                getActivity().startActivity(intent);
            }
        });
        tvWaitEveluate = (TextView) view.findViewById(R.id.my_wait_eveluate_order_num);
        tvWaitGet = (TextView) view.findViewById(R.id.wait_get_order_num);
        tvWaitPay = (TextView) view.findViewById(R.id.wait_pay_order_num);
        tvAllOrder = (TextView) view.findViewById(R.id.shop_order_num);
        tvRrfundOrder = (TextView) view.findViewById(R.id.my_refund_order_num);

    }

    private void onUnReadMessage() {
        if (VariableUtil.messageNum >= MAX_MASSAGE) {
            btnMessage.setUnReadCount(MAX_MASSAGE);
        } else {
            btnMessage.setUnReadCount(VariableUtil.messageNum);
        }
    }

    private void initData() {
        for (int i = 0; i < 6; i++) {
            if (VariableUtil.BoolCode) {
                if (i != 1 && i != 2) {
                    HashMap<String, Integer> map = new HashMap<String, Integer>();
                    map.put("code", i);
                    mList.add(map);
                }
            } else {
                HashMap<String, Integer> map = new HashMap<String, Integer>();
                map.put("code", i);
                mList.add(map);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void goActivity() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int code = mList.get(position).get("code");
                switch (code) {
                    case 0:
                        if (VariableUtil.loginStatus) {
                            goMyWallet();
                        } else {
                            goLogin();
                        }
                        break;
                    case 1:
                        if (VariableUtil.loginStatus) {
                            goGasServer();
                        } else {
                            goLogin();
                        }
                        break;
                    case 2:
                        if (VariableUtil.loginStatus) {
                            goCustomerNo();
                        } else {
                            goLogin();
                        }
                        break;
                    case 3:
                        if (VariableUtil.loginStatus) {
                            goInvoice();
                        } else {
                            goLogin();
                        }
                        break;
                    case 4:
                        if (VariableUtil.loginStatus) {
                            goManage();
                        } else {
                            goLogin();
                        }
                        break;
                    case 5:
                        goShare();
                        break;
                    default:
                        break;
                }

            }
        });
    }

    private void goLogin() {
        Intent login = new Intent(mContext, LoginActivity.class);
        startActivity(login);
    }

    private void onGetAvatar() {
        SendRequestUtil.getBitmapFromService(avatarUrl, getImageHandler);
    }

    private void initListeners() {
        ViewTreeObserver vto = layoutMySetting.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layoutMySetting.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                bgHeight = layoutMySetting.getHeight();
                inisetListaner();
            }
        });
    }

    private void inisetListaner() {
        myScrollview.setScrollViewListener(this);
    }

    @Override
    public void onScrollChanged(MyScrollview scrollView, int l, int t, int oldl, int oldt) {
        if (t <= 0) {
            //设置标题的背景颜色
            layoutNavigation.setBackgroundColor(Color.argb(0, 0, 255, 0));
            tvNavigation.setTextColor(Color.argb(0, 0, 255, 0));
            //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
        } else if (t > 0 && t <= bgHeight) {
            float scale = (float) t / bgHeight;
            float alpha = (255 * scale);
            tvNavigation.setTextColor(Color.argb((int) alpha, 255, 255, 255));
            layoutNavigation.setBackgroundColor(Color.argb((int) alpha, 249, 99, 49));
        } else {    //滑动到banner下面设置普通颜色
            layoutNavigation.setBackgroundColor(Color.argb(255, 249, 99, 49));
            tvNavigation.setTextColor(Color.argb(255, 255, 255, 255));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_re_gosetting:
                if (VariableUtil.loginStatus) {
                    goSetting();
                } else {
                    goLogin();
                }
                break;
            case R.id.id_re_consult:
                if (VariableUtil.loginStatus) {
                    goConsult();
                } else {
                    goLogin();
                }
                break;
            case R.id.id_re_hotline:
                hotLine();
                break;
            case R.id.id_re_setting:
                goMoreSetting();
                break;
            case R.id.id_right_massage:
                goMessageCenter();
                break;
            default:
                break;
        }
    }

    private void goMessageCenter() {
        Intent intent = new Intent(mContext, MessageCenterActivity.class);
        startActivity(intent);
        btnMessage.setUnReadCount(0);
    }

    private void hotLine() {
        final String phone = "963666";
        new PromptDialog.Builder(mContext)
                .setTitle("服务热线")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(phone)
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("呼叫", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        requestLimit(phone);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void goConsult() {
        Intent intent = new Intent(mContext, ConsultRecommend.class);
        startActivity(intent);
    }

    private void goShare() {
        Intent intent = new Intent(mContext, ShareMenuActivity.class);
        startActivity(intent);
    }

    private void goManage() {
        Intent intent = new Intent(mContext, AddressManageActivity.class);
        startActivity(intent);
    }

    private void goMyWallet() {
        Intent intent = new Intent(mContext, MyWalletActivity.class);
        startActivityForResult(intent, 0x004);
    }

    private void goInvoice() {
        Intent intent = new Intent(mContext, InvoiceOpen.class);
        startActivity(intent);
    }

    private void goGasServer() {
        Intent intent = new Intent(mContext, GasServerOrderActivity.class);
        startActivity(intent);
    }

    private void goSetting() {
        Intent intent = new Intent(mContext, Mysetting.class);
        startActivityForResult(intent, 1);
    }

    private void goCustomerNo() {
        Intent intent = new Intent(mContext, CustomerNoManage.class);
        startActivity(intent);
    }

    private void goMoreSetting() {
        Intent intent = new Intent(mContext, MoreSetting.class);
        startActivityForResult(intent, 0x005);
    }

    private void requestLimit(final String phone) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                CallPhoneUtil.callPhone(mContext, phone);
            } else {
                MPermissionUtils.requestPermissionsResult(this, MY_PERMISSIONS_REQUEST_CALL_PHONE, new String[]{Manifest.permission.CALL_PHONE}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted(int code) {
                        if (code == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
                            CallPhoneUtil.callPhone(mContext, phone);
                        }
                    }

                    @Override
                    public void onPermissionDenied(int code) {
                        ToastUtil.ToastText(mContext, "没有权限您将无法进行相关操作！");
                    }
                });
            }
        } else {
            CallPhoneUtil.callPhone(mContext, phone);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);

    }

    ;

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
}
