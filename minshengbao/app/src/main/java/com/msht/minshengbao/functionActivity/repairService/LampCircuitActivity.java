package com.msht.minshengbao.functionActivity.repairService;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.adapter.SanitaryAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/6/20  
 */
public class LampCircuitActivity extends BaseActivity {
    private WebView webView;
    private MyNoScrollGridView mGridView;
    private SanitaryAdapter homeAdapter;
    private String pid;
    private String cityId;
    private CustomDialog customDialog;
    private   boolean loginState =false;
    private static final String SERVICE_URL = UrlUtil.Service_noteUrl;
    private ArrayList<HashMap<String, String>> functionList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandle=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<LampCircuitActivity> mWeakReference;
        private RequestHandler(LampCircuitActivity activity) {
            mWeakReference = new WeakReference<LampCircuitActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LampCircuitActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
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
                        JSONArray json =object.getJSONArray("data");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.initFunction(json);
                        }else {
                            ToastUtil.ToastText(activity.context,error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);

        }
    }
    private void initFunction(JSONArray json) {
        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject jsonObject = json.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String code = jsonObject.getString("code");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("name", name);
                map.put("code", code);
                functionList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (functionList.size() != 0) {
            homeAdapter.notifyDataSetChanged();
        } else {

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp_circuit);
        setCommonHeader("灯具电路");
        context=this;
        loginState = SharedPreferencesUtil.getLstate(this, SharedPreferencesUtil.Lstate, false);
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        pid=data.getStringExtra("pid");
        cityId=data.getStringExtra("city_id");
        initView();
        homeAdapter=new SanitaryAdapter(context,functionList);
        mGridView.setAdapter(homeAdapter);
        initData();
        iniWebView();
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (loginState){
                    String mId=functionList.get(position).get("id");
                    String mName=functionList.get(position).get("name");
                    Intent intent=new Intent(context,PublishOrderActivity.class);
                    intent.putExtra("id",mId);
                    intent.putExtra("name",mName);
                    intent.putExtra("maintype",mPageName);
                    startActivity(intent);
                }else {
                    Intent login=new Intent(context, LoginActivity.class);
                    startActivity(login);
                    finish();
                }
            }
        });
    }
    private void initView() {
        mGridView=(MyNoScrollGridView)findViewById(R.id.id_function_view);
        webView=(WebView)findViewById(R.id.id_webview);
    }
    private void initData() {
        customDialog.show();
        String functionUrl= UrlUtil.SecondService_Url;
        String function="";
        try {
            function =functionUrl +"?city_id="+ URLEncoder.encode(cityId, "UTF-8")+"&pid="+URLEncoder.encode(pid, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(function, OkHttpRequestUtil.TYPE_GET,null,requestHandle);
    }
    private void iniWebView() {
        webView.loadUrl(SERVICE_URL);
        WebSettings settings=webView.getSettings();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setDisplayZoomControls(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.requestFocusFromTouch();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.toString());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
