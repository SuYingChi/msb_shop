package com.msht.minshengbao.functionActivity.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.adapter.CouponAdapter;
import com.msht.minshengbao.Base.BaseFragment;
import com.msht.minshengbao.functionActivity.MyActivity.ShareMenuActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * @author hong
 */
public class CouponFragment extends BaseFragment {
    private String userId;
    private String password;
    private  String status="1";
    private RelativeLayout layoutNoData;
    private Button btnShare;
    private XListView xListView;
    private CouponAdapter mAdapter;
    private int pageNo=1;
    private int pageIndex=0;
    private int refreshType;
    private JSONArray jsonArray;
    private final String mPageName = "优惠券";
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> couponList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    public CouponFragment() {}
    public static CouponFragment getinstance(int position) {
        CouponFragment couponFragment = new CouponFragment();
        switch (position){
            case 0:
                couponFragment.status="1";
                break;
            case 1:
                couponFragment.status="2";
                break;
            case 2:
                couponFragment.status="3";
            default:
                break;
        }
        return couponFragment ;
    }
    private static class RequestHandler extends Handler{
        private WeakReference<CouponFragment> mWeakReference;
        public RequestHandler(CouponFragment couponFragment) {
            mWeakReference = new WeakReference<CouponFragment>(couponFragment);
        }
        @Override
        public void handleMessage(Message msg) {
            final CouponFragment reference =mWeakReference.get();
            // the referenced object has been cleared
            if (reference == null||reference.isDetached()) {
                return;
            }
            if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                reference.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String error = object.optString("error");
                        reference.jsonArray =object.optJSONArray("data");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (reference.refreshType==0){
                                reference.xListView.stopRefresh(true);
                            }else if (reference.refreshType==1){
                                reference.xListView.stopLoadMore();
                            }
                            if(reference.jsonArray.length()>0){
                                if (reference.pageNo==1){
                                    reference.couponList.clear();
                                }
                            }
                            reference.onGetCouponData();
                        }else {
                            reference.onFaifure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(reference.mContext,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onGetCouponData() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id=jsonObject.optString("id");
                String name=jsonObject.optString("name");
                String scope = jsonObject.getString("scope");
                String amount = jsonObject.getString("amount");
                String useLimit = jsonObject.getString("use_limit");
                String startDate = jsonObject.getString("start_date");
                String endDate= jsonObject.getString("end_date");
                String remainderDays="";
                if (jsonObject.has("remainder_days")){
                    remainderDays=jsonObject.optString("remainder_days");
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("name",name);
                map.put("scope", scope);
                map.put("amount", amount);
                map.put("use_limit", useLimit);
                map.put("start_date",startDate);
                map.put("end_date", endDate);
                map.put("remainder_days",remainderDays);
                map.put("type",status);
                couponList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (couponList.size()==0){
            if (status.equals(VariableUtil.VALUE_ONE)){
                layoutNoData.setVisibility(View.VISIBLE);
            }else {
                layoutNoData.setVisibility(View.GONE);
            }
        }else {
            layoutNoData.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }
    private void onFaifure(String error) {
        new PromptDialog.Builder(getActivity())
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==1){
                    initData();
                }
                break;
            default:
                break;
        }
    }
    @Override
    public View initView() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_coupon, null, false);
        customDialog=new CustomDialog(getActivity(), "正在加载");
        userId= SharedPreferencesUtil.getUserId(getActivity(), SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(getActivity(), SharedPreferencesUtil.Password,"");
        initMyView(mRootView);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ShareMenuActivity.class);
                startActivityForResult(intent,1);
            }
        });
        return mRootView;
    }

    private void initMyView(View mRootView) {
        layoutNoData =(RelativeLayout)mRootView.findViewById(R.id.id_re_nodata);
        btnShare =(Button)mRootView.findViewById(R.id.id_btn_share);
        xListView =(XListView)mRootView.findViewById(R.id.id_dicount_mlistview);
        xListView.setPullLoadEnable(true);
        mAdapter=new CouponAdapter(getActivity(),couponList);
        xListView.setAdapter(mAdapter);
        xListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                refreshType=0;
                loadData(1);
            }
            @Override
            public void onLoadMore() {
                refreshType=1;
                loadData(pageIndex + 1);
            }
        });
    }
    @Override
    public void initData() {
        customDialog.show();
        loadData(1);
    }
    private void loadData(int i) {
        pageIndex =i;
        pageNo=i;
        String validateURL = UrlUtil.Counpon_Url;
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("status",status);
        textParams.put("page",pageNum);
        OkHttpRequestUtil.getInstance(mContext.getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);

    };
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
    @Override
    public void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
