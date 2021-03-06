package com.msht.minshengbao.functionActivity.repairService;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.adapter.PayWayAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.functionActivity.Public.PaySuccessActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.pingplusplus.android.Pingpp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/6/28 
 */
public class RepairPaymentActivity extends BaseActivity  {
    private Button   btnSend;
    private String  userId,password,couponId="0";
    private String realMoney,channel,orderNo,type,orderId;
    private String id ;
    private String charge;
    private JSONArray jsonArray;
    private ListViewForScrollView forScrollView;
    private PayWayAdapter mAdapter;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private int requestCode=0;
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    private final ChargeHandler chargeHandler=new ChargeHandler(this);
    private final RequestHandler requestHandler =new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<RepairPaymentActivity> mWeakReference;
        public RequestHandler(RepairPaymentActivity activity) {
            mWeakReference = new WeakReference<RepairPaymentActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final RepairPaymentActivity reference =mWeakReference.get();
            if (reference == null||reference.isFinishing()) {
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
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (reference.requestCode==0){
                                reference.jsonArray =object.getJSONArray("data");
                                reference.onPayWayShow();
                            }else if (reference.requestCode==2){
                                JSONObject json=object.getJSONObject("data");
                                reference.onPayResult(json);
                            }
                        }else {
                            reference.onShowFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    reference.onShowFailure(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class ChargeHandler extends Handler{
        private WeakReference<RepairPaymentActivity> mWeakReference;
        private ChargeHandler(RepairPaymentActivity activity) {
            mWeakReference = new WeakReference<RepairPaymentActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final RepairPaymentActivity activity =mWeakReference.get();
            if (activity == null||activity.isFinishing()) {
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String error = object.optString("error");
                        activity.jsonObject =object.optJSONObject("data");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode==0){
                                activity.onShowBalance();
                            }else if (activity.requestCode==1){
                                activity.onReceiveChargeData();
                            }
                        }else {
                            activity.onShowFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onPayResult(JSONObject json) {
        String status=json.optString("status");
        String chargeId=json.optString("chargeId");
        String lottery=json.optString("lottery");
        switch (status){
            case VariableUtil.VALUE_ZERO:
                onStartSuccess("1",lottery);
                break;
            case VariableUtil.VALUE_ONE:
                onStartSuccess("1",lottery);
                break;
            case VariableUtil.VALUE_TWO:
                onStartSuccess("5",lottery);
                break;
            case VariableUtil.VALUE_THREE:
                onShowDialogs("正在支付");
                break;
                default:
                    break;
        }
    }

    private void onStartSuccess(String successType, String lottery) {
        Intent success=new Intent(context,PaySuccessActivity.class);
        success.putExtra("type",successType);
        success.putExtra("url",lottery);
        success.putExtra("orderId",orderId);
        startActivity(success);
        finish();
    }

    private void onPayWayShow() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String tips = json.getString("tips");
                String name=json.getString("name");
                String code=json.getString("code");
                String channel=json.getString("channel");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("tips", tips);
                map.put("name",name);
                map.put("code",code);
                map.put("channel",channel);
                mList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (mList.size()!=0){
            mAdapter.notifyDataSetChanged();
        }
    }
    private void onShowBalance() {
        double doubleBalance=jsonObject.optDouble("balance");
        double doubleAmount=Double.valueOf(realMoney);
        if (doubleAmount<=doubleBalance){
            VariableUtil.balance="余额充足";
        }else {
            VariableUtil.balance="余额不足";
        }
        initPayWay();
    }
    private void onShowFailure(String error) {
        new PromptDialog.Builder(this)
                .setTitle("缴费提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    private void onReceiveChargeData() {
        try {
            id = jsonObject.optString("id");
            charge = jsonObject.optString("charge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (channel.equals(VariableUtil.VALUE_ONE)||channel.equals(VariableUtil.VALUE_THREE)||channel.equals(VariableUtil.VALUE_FIVE)||channel.equals(VariableUtil.VALUE_SEVER)) {
            Pingpp.createPayment(RepairPaymentActivity.this, charge);
        }else if (channel.equals(VariableUtil.VALUE_EIGHT)||channel.equals(VariableUtil.VALUE_SIX)){
            setResult(0x005);
            Intent success=new Intent(context,PaySuccessActivity.class);
            success.putExtra("type","1");
            success.putExtra("url","");
            success.putExtra("orderId",orderId);
            startActivity(success);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_payment);
        setCommonHeader("维修支付");
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        orderNo=data.getStringExtra("orderNo");
        orderId=data.getStringExtra("orderId");
        couponId=data.getStringExtra("couponId");
        realMoney =data.getStringExtra("realMoney");
        type="2";
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initFindViewId();
        mAdapter=new PayWayAdapter(context, mList);
        forScrollView.setAdapter(mAdapter);
        initData();
        initEvent();
        mAdapter.setOnItemClickListener(new PayWayAdapter.OnRadioItemClickListener() {
            @Override
            public void itemClick(View view, int thisPosition) {
                btnSend.setEnabled(true);
                VariableUtil.payPos =thisPosition;
                mAdapter.notifyDataSetChanged();
                channel= mList.get(thisPosition).get("channel");
            }
        });
    }
    private void initFindViewId() {
        forScrollView=(ListViewForScrollView)findViewById(R.id.id_payway_view);

        TextView tvBalance =(TextView)findViewById(R.id.id_tv_balance);
        btnSend =(Button)findViewById(R.id.id_evaluate_order);
        btnSend.setEnabled(false);
        TextView tvRealAmount =(TextView)findViewById(R.id.id_real_fee);
        TextView tvOrderNo =(TextView)findViewById(R.id.id_orderNo);
        tvRealAmount.setText("¥"+ realMoney);
        tvOrderNo.setText(orderNo);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            //if (requestCode == REQUEST_CODE_PAYMENT){
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                // 错误信息
                String errorMsg = data.getStringExtra("error_msg");
                String extraMsg = data.getStringExtra("extra_msg");
                showMsg(result, errorMsg, extraMsg);
            }
        }
    }
    private void showMsg(String title, String msg1, String msg2) {
        String str;
        switch (title){
            case SendRequestUtil.SUCCESS_VALUE:
                str="缴费成功";
                setResult(0x005);
                requestResult();
                break;
            case SendRequestUtil.FAILURE_VALUE:
                str="缴费失败";
                onShowDialogs(str);
                break;
            case SendRequestUtil.CANCEL_VALUE:
                str="已取消缴费";
                onShowDialogs(str);
                break;
                default:
                    break;
        }
    }
    private void onShowDialogs(String str) {
        new PromptDialog.Builder(this)
                .setTitle("支付提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(str)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }
    private void initData() {
        requestCode=0;
        String validateURL= UrlUtil.Mywallet_balanceUrl;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,chargeHandler);
    }
    private void initEvent() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips();
            }
        });
    }
    private void showTips() {
        new PromptDialog.Builder(context)
                .setTitle("提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("请确认是否要进行缴费")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        customDialog.show();
                        requestCode=1;
                        requestService();
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void requestService() {
        String validateURL= UrlUtil.PayfeeWay_Url;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("type",type);
        textParams.put("amount", realMoney);
        textParams.put("orderId",orderId);
        if (!TextUtils.isEmpty(couponId)) {
            textParams.put("couponId", couponId);
        }
        textParams.put("channel",channel);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,chargeHandler);
    }
    private void initPayWay() {
        String source="repair_order";
        customDialog.show();
        String validateURL= UrlUtil.PAY_METHOD_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("source",source);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void requestResult() {
        requestCode=2;
        String validateURL= UrlUtil.PAY_RESULT_NOTARIZE;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
}
